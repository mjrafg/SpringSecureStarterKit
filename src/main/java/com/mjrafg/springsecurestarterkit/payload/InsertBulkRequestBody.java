package com.mjrafg.springsecurestarterkit.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsertBulkRequestBody<T> {
    private List<T> rows;
}
