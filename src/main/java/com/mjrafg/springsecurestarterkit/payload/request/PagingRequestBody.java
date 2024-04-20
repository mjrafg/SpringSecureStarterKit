package com.mjrafg.springsecurestarterkit.payload.request;

import com.mjrafg.springsecurestarterkit.payload.FilterParams;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagingRequestBody {
    private List<FilterParams> filters =new ArrayList<>();
    private int pageNum;
    private int pageSize;
    private boolean exportExcel;
    private boolean exportExcelExample;
    private String orderColumn="id";
    private String orderSort="asc";
}
