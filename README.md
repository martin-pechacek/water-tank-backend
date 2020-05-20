# Water Tank

Backend part of measuring water level in tank solution.

__Other parts of solution__
* [Arduino with ultrasonic sensor](https://github.com/martin-pechacek/water-tank-arduino)
* [Mobile app](https://github.com/martin-pechacek/water-tank-app)

## Notes
* For successful api communication client has to contain correct device id in header
* Dev profile uses HTTP communication and in-memory [H2 database](https://www.h2database.com/)
* Prod profile uses self-signed certificate and HTTPS only.
* Certificate is saved in machine's JAVA_HOME/certificate folder

## License
[GNU GPLv3](https://choosealicense.com/licenses/gpl-3.0/)