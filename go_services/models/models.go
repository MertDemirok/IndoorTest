package models

type Location struct {
	Id      int    `db:"id"`
	MacID 	string `db:"mac_id"`
	SSID    string `db:"ssid"`
	RSSIMin string `db:"rssi_min"`
	RSSIMax string `db:"rssi_max"`
}
