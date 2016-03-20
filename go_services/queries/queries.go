package queries

import "github.com/jmoiron/sqlx"

type Prepared struct {
	GetLocationData *sqlx.Stmt
	InsertApData    *sqlx.Stmt
	InsertLocData   *sqlx.Stmt
	InsertLocDef    *sqlx.Stmt
}

func RegisterStatements(db *sqlx.DB) (*Prepared, error) {
	prepared := &Prepared{}
	var err error

	prepared.GetLocationData, err = db.Preparex(`SELECT * FROM location_data`)
	prepared.InsertApData, err = db.Prepare(`INSERT INTO ap_data(mac, rssi_max, rssi_min, loc_def_id) VALUES($1, $2, $3, $4) RETURNING id`)
	prepared.InsertLocData, err = db.Preparex(`INSERT INTO loc_data(device_id, loc_def_id) VALUES($1, $2) RETURNING id`)
	prepared.InsertLocDef, err = db.Preparex(`INSERT INTO loc_def(name) VALUES($1) RETURNING id`)
	//prepared.InsertLocationData, err = db.Preparex(`INSERT INTO location_data(mac_id, ssid, rssi_min, rssi_max, id) VALUES($1, $2, $3, $4, $5)`)

	return prepared, err
}
