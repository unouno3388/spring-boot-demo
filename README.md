如果想在本地測試(也就是codespace中)請做以下操作
1.請在resources資料夾中添加名為application-local.properties檔案
內容
spring.application.name=server
server.forward-headers-strategy=framework
server.use-forward-headers=true
spring.mvc.servlet.load-on-startup=1
spring.main.lazy-initialization=false
logging.level.org.springframework.web.servlet.DispatcherServlet=DEBUG
logging.level.org.springframework.web.socket=DEBUG
server.servlet.session.tracking-modes=cookie
logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.security=DEBUG
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false 
spring.web.resources.static-locations=classpath:/static/
spring.mvc.static-path-pattern=/**
spring.thymeleaf.mode=HTML
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.servlet.content-type=text/html
spring.thymeleaf.check-template-location=true
spring.mvc.view.prefix=classpath:/templates/
spring.mvc.view.suffix=.html
server.tomcat.redirect-context-root=false
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
#spring.datasource.url=jdbc:h2:file:/home/codespace/test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.settings.web-allow-others=true
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
#spring.h2.console.enabled=true
#spring.h2.console.path=/h2-console 

並在終端機設立環境變數export SPRING_PROFILES_ACTIVE=local
這樣本地測試會使用無持久化的H2Data Base

2.請確認mvnw的java版本是否為17
cd到server層 輸入指令
chmod +x ./mvnw 
./mvnw -v
如不是請再進行以下指令操作
cat /etc/os-release 看linux是甚麼版本 必須是Debian or Ubuntu才有apt
sudo apt update   
sudo apt install openjdk-17-jdk
sudo update-alternatives --config java
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
