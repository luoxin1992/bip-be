/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext;

import cn.edu.xmu.sy.ext.mapper.CounterMapper;
import cn.edu.xmu.sy.ext.mapper.FingerprintMapper;
import cn.edu.xmu.sy.ext.mapper.UserMapper;
import cn.edu.xmu.sy.ext.service.TtsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author luoxin
 * @version 2017-6-14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataImporter {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FingerprintMapper fingerprintMapper;
    @Autowired
    private CounterMapper counterMapper;

    @Autowired
    private TtsService ttsService;

    @Test
    public void importCounter() throws Exception {
        //String[] names = new String[]{"领枪", "交枪", "领弹", "交弹", "物品领用", "本地测试"};
        //String[] ips = new String[]{"192.168.30.63", "192.168.30.64", "192.168.30.61", "192.168.30.62",
        //        "192.168.30.23", "192.168.30.20"};
        //for (int i = 1; i <= names.length; i++) {
        //    CounterDO domain = new CounterDO();
        //    domain.setNumber("0" + String.valueOf(i));
        //    domain.setName(names[i - 1]);
        //    domain.setIp(ips[i - 1]);
        //    domain.setMac(UUIDUtil.randomHexUUID() + UUIDUtil.randomHexUUID());
        //    counterMapper.save(domain);
        //}
    }

    /**
     * select a.system_user_id, a.system_user_real_name, b.photo_path, a.fpcode
     * from store_system_user as a
     * left join (select id, photo_path
     * from store_system_dispatcher
     * where store_system_dispatcher.status = 0
     * ) as b
     * on a.system_user_id = b.id
     * where a.system_user_id regexp '[0-9]+'
     */
    @Test
    public void importUserAndFingerprint() throws Exception {
        //try (BufferedReader br = new BufferedReader(new FileReader("/home/luoxin/桌面/333.csv"))) {
        //    String line;
        //    while ((line = br.readLine()) != null) {
        //        String[] data = line.split(",");
        //
        //        UserDO user = new UserDO();
        //        user.setNumber(data[0]);
        //        user.setName(data[1]);
        //        if (!data[2].equals("NULL")) {
        //            user.setPhoto("http://112.5.65.151:9001" + data[2]);
        //        }
        //        userMapper.save(user);
        //
        //        if (!data[3].equals("NULL")) {
        //            FingerprintDO fingerprint = new FingerprintDO();
        //            fingerprint.setUserId(user.getId());
        //            fingerprint.setUid(UIDGenerateUtil.Compact.nextId());
        //            fingerprint.setFinger(FingerprintFingerEnum.UNKNOWN.getTag());
        //            fingerprint.setTemplate(data[3]);
        //            fingerprintMapper.save(fingerprint);
        //        }
        //    }
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
    }

    @Test
    public void synthesizeUserNameVoice() throws Exception {
        //List<UserDO> users = userMapper.listAll(0L, 1000);
        //List<String> names = users.stream()
        //        .map(UserDO::getName)
        //        .collect(Collectors.toList());
        //ttsService.ttsBatchSync(names, false);
    }
}
