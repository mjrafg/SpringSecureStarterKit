package com.mjrafg.springsecurestarterkit.app.base.company;


import com.mjrafg.springsecurestarterkit.base.repository.BaseRepository;

public interface CompanyRepository  extends BaseRepository<CompanyEntity, String> {
    boolean existsByCodeAndCodeNot(String searchCode,String currentCode);
}
