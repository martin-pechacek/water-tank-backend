create table measurement_history (tank_fullness integer, created_at timestamp(6), id bigint generated by default as identity, device varchar(255), primary key (id));
