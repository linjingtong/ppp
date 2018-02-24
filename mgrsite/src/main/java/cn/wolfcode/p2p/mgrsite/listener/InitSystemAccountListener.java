package cn.wolfcode.p2p.mgrsite.listener;

import cn.wolfcode.p2p.bussiness.service.ISystemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class InitSystemAccountListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ISystemAccountService systemAccountService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        systemAccountService.initSystemAccount();
    }
}
