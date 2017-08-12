package cn.edu.xmu.sy.ext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication(scanBasePackages = {"cn.com.lx1992.lib", "cn.edu.xmu.sy.ext"})
@EnableWebSocket
@EnableScheduling
public class Application {
    private static final String APP_PROPS_FILE = "META-INF/app.properties";

    public static void main(String[] args) {
        if (loadAppProps()) {
            SpringApplication application = new SpringApplication(Application.class);
            application.setBannerMode(Banner.Mode.OFF);
            application.run(args);
        }
    }

    private static boolean loadAppProps() {
        try (InputStream appPropsFile = Application.class.getClassLoader().getResourceAsStream(APP_PROPS_FILE)) {
            Properties properties = new Properties();
            properties.load(appPropsFile);
            properties.forEach((key, value) -> {
                if (StringUtils.isEmpty(System.getProperty(String.valueOf(key)))) {
                    System.out.println("loading app property: key=" + key + ", value=" + value);
                    System.setProperty(String.valueOf(key), String.valueOf(value));
                }
            });
            return true;
        } catch (Exception e) {
            System.err.println("error loading app properties");
            e.printStackTrace();
        }
        return false;
    }
}
