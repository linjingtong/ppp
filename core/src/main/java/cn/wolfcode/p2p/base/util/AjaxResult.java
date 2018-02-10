package cn.wolfcode.p2p.base.util;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class AjaxResult {

    private boolean success = true;
    private String msg;

    public AjaxResult(boolean success,String msg){
        this.success=success;
        this.msg=msg;
    }
    public AjaxResult(String msg){
        this.msg=msg;
    }
}
