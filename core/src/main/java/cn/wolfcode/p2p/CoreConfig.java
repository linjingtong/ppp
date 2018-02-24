package cn.wolfcode.p2p;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@MapperScan({"cn.wolfcode.p2p.base.mapper","cn.wolfcode.p2p.bussiness.mapper"})
@PropertySources({
    @PropertySource("classpath:email.properties")
})
public class CoreConfig {

}
