package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.query.UserFileQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by seemygo on 2018/1/24.
 */
public interface IUserFileService {

    int save(UserFile record);

    UserFile get(Long id);

    List<UserFile> selectAll();

    int update(UserFile record);

    PageInfo query(UserFileQueryObject qo);

    /**
     *将选择的材料保存到数据库
     * @param fileName
     */
    void saveFirst(String fileName);


    /**
     * 查询FileType为空的数据
     * @return
     */
    List<UserFile> getHasNullFileType();

    /**
     *批量添加材料
     * @param fileTypes
     * @param ids
     */
    void addFileType(Long[] fileTypes, Long[] ids);

    void audit(Long id, int state, int score, String remark);

    List<UserFile> queryListByApplierId(Long applierId);
}
