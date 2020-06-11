package cn.minsin.core.rule;

import cn.minsin.core.override.FieldCheck;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: minton.zhang
 * @since: 2020/3/31 21:54
 */
public abstract class AbstractModelRule implements FieldCheck {

    /**
     *
     */
    private static final long serialVersionUID = 57625408003186203L;

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public String toString() {
        //移除校验 since 0.2.7
        return JSON.toJSONString(this);
    }
}

