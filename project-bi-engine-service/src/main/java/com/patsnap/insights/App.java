package com.patsnap.insights;

import com.patsnap.insights.trickydata.manager.ExcelManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mvn archetype spring boot
 * TODO: del @RestController / func home
 */
@RestController
@SpringBootApplication(scanBasePackages = {"com.patsnap.insights"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class App {

    @Autowired
    ExcelManager excelManager;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(App.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        application.run(args);
    }

    @GetMapping(value = "/readExcel")
    public String readExcel() {
        return excelManager.readFile();
    }

}
