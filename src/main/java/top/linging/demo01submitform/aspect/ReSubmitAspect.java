package top.linging.demo01submitform.aspect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jodd.time.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.linging.demo01submitform.anno.CheckSubmitForm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Linging
 * @version 1.0.0
 * @since 1.0
 */
@Component
@Aspect
@Slf4j
public class ReSubmitAspect {

    @Autowired
    private RedissonClient redissonClient;


    @Around("@annotation(top.linging.demo01submitform.anno.CheckSubmitForm)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        MethodSignature signature = (MethodSignature) point.getSignature();
        CheckSubmitForm annotation = signature.getMethod().getAnnotation(CheckSubmitForm.class);
        if(annotation == null){ //没有限制则直接放行
            return point.proceed(args);
        }else{
            String formId = null;
            for (Object arg : args) {
                if (StringUtils.isEmpty(formId)) {
                    JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(arg));
                    if (!StringUtils.isEmpty(jsonObject.getString("key"))) {
                        formId = jsonObject.getString("key");
                        System.out.println(formId);
                    }
                }
            }
            RBucket<Object> bucket = redissonClient.getBucket(formId);
            String o = (String) bucket.get();
            if(o != null){
                System.out.println("重复提交了==>" + o);
                HashMap<String, Object> map = new HashMap<>();
                map.put("data","重复提交：" + formId);
                map.put("code",200);
                return map;
            }else{
                bucket.set(JSONObject.toJSONString(args[0]),annotation.retrySecond(), TimeUnit.SECONDS);
                return point.proceed(args);
            }
        }
    }
}
