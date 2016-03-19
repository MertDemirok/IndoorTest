package queries

import "github.com/jmoiron/sqlx"

type Prepared struct {
	GetLocationData   		*sqlx.Stmt
	InsertLocationData      *sqlx.Stmt
}
func RegisterStatements(db *sqlx.DB) (*Prepared, error) {
	prepared := &Prepared{}
	var err error

	prepared.GetLocationData, err = db.Preparex(`SELECT * FROM location_data`)
	prepared.InsertLocationData, err = db.Preparex(`INSERT INTO location_data(mac_id, ssid, rssi_min, rssi_max, id) VALUES($1, $2, $3, $4, $5)`)

	return prepared, err
}