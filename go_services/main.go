/*
	author nihatemreyuksel
*/

package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/Emre/indoor_service/indoor_test/go_services/queries"
	"github.com/Emre/indoor_service/indoor_test/go_services/models"
	"github.com/gorilla/mux"
	"github.com/c3mb0/beter"
	"github.com/jmoiron/sqlx"
	_ "github.com/lib/pq"
)

var db *sqlx.DB
var stmt *queries.Prepared

type exit struct{ Code int }

func handleExit() {
	if e := recover(); e != nil {
		if exit, ok := e.(exit); ok == true {
			os.Exit(exit.Code)
		}
		panic(e)
	}
}

func init() {
	log.SetFlags(log.Ltime | log.Lshortfile)
}

func check(err error) {
	if err != nil {
		details := err.(*b.B)
		fmt.Printf("%s\n%s:%d\n", details.Err, details.Fn, details.Line)
		os.Exit(1)
	}
}

func reqErr(err error, w http.ResponseWriter) {
	if err != nil {
		log.Println(err)
		w.WriteHeader(400)
		return
	}
}

func prepareDb() {
	var err error
	db = sqlx.MustConnect("postgres", "user=postgres password=19071993 dbname=indoor_location sslmode=disable")
	stmt, err = queries.RegisterStatements(db)
	check(b.E(err))
}

func main() {
	defer handleExit()

	prepareDb()

	router := mux.NewRouter().StrictSlash(true)

	router.HandleFunc("/check", areWeLive).Methods("GET")
	router.HandleFunc("/getLocationData", getLocationData).Methods("GET")
	router.HandleFunc("/insertLocationData", insertLocationData).Methods("POST")

	http.ListenAndServe(":2323", router)
}

func areWeLive(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("It's working!"))
}

func getLocationData(w http.ResponseWriter, r *http.Request) {
	
	location := []models.Location{}
	err := stmt.GetLocationData.Select(&location)

	//Location is not found!
	reqErr(err, w)

	//Data is fetching
	jsonData, _ := json.Marshal(location)
	w.Write(jsonData)
}

func insertLocationData(w http.ResponseWriter, r *http.Request){
	decoder := json.NewDecoder(r.Body)
	var location models.Location
	err := decoder.Decode(&location)
	check(b.E(err))

	_, err2 := stmt.InsertLocationData.Exec(location.MacID, location.SSID, location.RSSIMin, location.RSSIMax, location.Id)
	check(b.E(err2))
}