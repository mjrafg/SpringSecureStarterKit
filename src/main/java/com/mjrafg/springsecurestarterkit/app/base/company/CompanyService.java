package com.mjrafg.springsecurestarterkit.app.base.company;

import com.mjrafg.springsecurestarterkit.base.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService  extends BaseServiceImpl<CompanyEntity,String> {
    CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        super(companyRepository);
        this.companyRepository = companyRepository;
    }
    public boolean checkCode(String searchCode, String currentCode) {
        return companyRepository.existsByCodeAndCodeNot(searchCode, currentCode);
    }
}
