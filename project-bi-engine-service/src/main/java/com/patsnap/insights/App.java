package com.patsnap.insights;


import com.patsnap.insights.trickydata.dao.RedshiftDao;
import com.patsnap.insights.trickydata.endpoint.request.ApiRequest;
import com.patsnap.insights.trickydata.manager.ApiManager;
import com.patsnap.insights.trickydata.manager.ExcelManager;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    @ResponseBody
    @RequestMapping(value = "/json", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String test() throws IOException {
        File file = ResourceUtils.getFile("classpath:data.json");
        InputStream inputStream = new FileInputStream(file);
        return IOUtils.toString(inputStream, "UTF-8");
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
