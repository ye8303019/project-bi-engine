package com.patsnap.insights;


import com.patsnap.insights.trickydata.dao.RedshiftDao;
import com.patsnap.insights.trickydata.endpoint.request.ApiRequest;
import com.patsnap.insights.trickydata.manager.ApiManager;
import com.patsnap.insights.trickydata.manager.ExcelManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @Autowired
    ApiManager apiManager;

    @Autowired
    RedshiftDao redshiftDao;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(App.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        application.run(args);
    }

    @GetMapping(value = "/")
    public String test() {
        List<Map<String, Object>> list = redshiftDao.getData("");
        return "success";
    }

    @PostMapping(value = "/api")
    public String apiResource(@RequestBody ApiRequest apiRequest) {
        return apiManager.generalJsonFile(apiRequest);
    }


    @PostMapping(value = "/file")
    public String fileResource(@RequestParam("file") MultipartFile file) throws IOException {
        return excelManager.generalJsonFile(file.getOriginalFilename(), file.getInputStream());
    }

}
