
# for Development

### install JAVA
```sh
sudo apt-get install openjdk-8-jdk openjdk-8-jre
```

### set PATH
##### edit /etc/profile
```sh
JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64"
PATH=$PATH:$JAVA_HOME
PLAY_HOME="/home/hip/play/play-2.2.1"
PATH=$PATH:$PLAY_HOME

export PLAY_HOME
export JAVA_HOME
export PATH
```

###  test run port 90
```sh
play run
```


# for test connect database server and run other port

### comment onStart methord in app/Global.java
```sh
// @Override
// 	public void onStart(Application app) {
// 	if(User.find.findRowCount() == 0) {
// 		Ebean.save((List) Yaml.load("initial-data.yml"));
// 		Ebean.save((List) Yaml.load("sternbergSearch.yml"));
// 		Ebean.save((List) Yaml.load("magicNumber7.yml"));
// 		Ebean.save((List) Yaml.load("simonEffect.yml"));
// 		Ebean.save((List) Yaml.load("mullerLayer.yml"));
// 		Ebean.save((List) Yaml.load("garnerInterference.yml"));
// 		Ebean.save((List) Yaml.load("visualSearch.yml"));
// 		Ebean.save((List) Yaml.load("changeBlindness.yml"));
// 		Ebean.save((List) Yaml.load("user.yml"));
// 	}
// }
```

### edit conf/application.conf

##### remove
```sh
 db.default.driver=org.h2.Driver
 db.default.url="jdbc:h2:mem:play"
 db.default.user=sa
 db.default.password=""
```
##### config
```sh
# db.default.driver=com.mysql.jdbc.Driver
# db.default.url="jdbc:mysql://localhost/[DATABASE_NAME]?characterEncoding=UTF-8"
# db.default.user=[USERNAME]
# db.default.password=["PASSWORD"]
```

##### test run
```sh
play "start <port>"
```

# for deployment

### in the Play console, simply type dist:
```sh
hip $ dist
```
### target/universal/hip-1.0-SNAPSHOT.zip
### unzip hip-1.0-SNAPSHOT.zip

```sh
unzip hip-1.0-SNAPSHOT.zip
```
### edit conf/application.conf

##### remove
```sh
 db.default.driver=org.h2.Driver
 db.default.url="jdbc:h2:mem:play"
 db.default.user=sa
 db.default.password=""
```
##### config
```sh
# db.default.driver=com.mysql.jdbc.Driver
# db.default.url="jdbc:mysql://localhost/[DATABASE_NAME]?characterEncoding=UTF-8"
# db.default.user=[USERNAME]
# db.default.password=["PASSWORD"]
```

### setup corn job
##### open corn job
```sh
sudo crontab -e
```
##### add
```sh
@reboot ( sleep 60 ; /home/hip/hip/target/universal/hip-1.0-SNAPSHOT/bin/hip -Dhttp.port=80 )
```