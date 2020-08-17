package com.weds.bean.base;

import com.weds.core.base.BaseEntity;

public interface DicFmtService {
    void DicFormat(BaseEntity baseEn, String[] typeCodes);

    void DicFormat(BaseEntity[] baseEns, String[] typeCodes);

    void DicFormat(BaseEntity baseEn, String typeCode);

    void DicFormat(BaseEntity[] baseEns, String typeCode);
}
