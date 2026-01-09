package com.example.plusproject.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SliceResponse<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private boolean hasNext;

    public static <T> SliceResponse<T> from(Slice<T> slice) {

        return new SliceResponse<>(
                slice.getContent(),
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext()
        );
    }
}

