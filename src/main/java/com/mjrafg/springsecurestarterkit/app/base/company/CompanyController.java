package com.mjrafg.springsecurestarterkit.app.base.company;

import com.mjrafg.springsecurestarterkit.base.controller.BaseController;
import com.mjrafg.springsecurestarterkit.payload.response.MainResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
public class CompanyController extends BaseController<CompanyEntity, String> {
    CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        super(companyService);
        this.companyService = companyService;
    }
    @GetMapping("/check/code/{searchCode}/{currentCode}")
    public ResponseEntity<?> checkUserName(@PathVariable String searchCode, @PathVariable String currentCode) {
        boolean isExist = companyService.checkCode(searchCode,currentCode);
        return MainResponse.ok(isExist);
    }
}
