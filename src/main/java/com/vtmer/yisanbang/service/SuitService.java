package com.vtmer.yisanbang.service;


import com.vtmer.yisanbang.dto.SuitDTO;

import java.util.List;

public interface SuitService {
    // 查找所有套装
    public List<SuitDTO> selectAll();

    // 根据套装id删除套装
    public boolean deleteSuitById(Integer suitId);

    // 根据套装id更新套装
    public boolean updateSuitById(SuitDTO suitDto);

    // 添加套装
    public boolean addSuit(SuitDTO suitDto);

    // 判断套装是否已经存在
    public boolean judgeSuit(SuitDTO suitDto, List<SuitDTO> suitDtoList);

    // 根据套装id查找套装
    public SuitDTO selectSuitById(Integer suitId);

    // 根据套装名字查找套装
    public SuitDTO selectSuitByName(String suitName);

    // 根据套装的最低价格排序进行显示
    public List<SuitDTO> selectSuitOrderByPrice();

    // 根据套装的时间排序进行显示
    public List<SuitDTO> selectSuitOrderByTime();

    // 根据分类id显示套装
    public List<SuitDTO> selectSuitBySort(Integer sortId);

    // 隐藏套装
    public boolean hideSuit(SuitDTO suitDto);
}
