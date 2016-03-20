package models

//Test : Location
type Location struct {
	Id      int    `db:"id"`
	MacID   string `db:"mac_id"`
	SSID    string `db:"ssid"`
	RSSIMin string `db:"rssi_min"`
	RSSIMax string `db:"rssi_max"`
}

type ApData struct {
	ID       int     `db:"id"`
	Mac      string  `db:"mac"`
	RssiMax  float32 `db:"rssi_max"`
	RssiMin  float32 `db:"rssi_min"`
	LocDefId int     `db:"loc_def_id"`
}

type LocData struct {
	ID       int    `db:"id"`
	DeviceID string `db:"device_id"`
	LocDefId int    `db:"loc_def_id"`
}

type LocDef struct {
	ID   int    `db:"id"`
	Name string `db:"name"`
}
