package com.deepthink.common.util;


import com.deepthink.common.web.ApiException;
import com.deepthink.common.web.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @Description : copy雪花算法
 * @Copyright   : Sinaif Software Co.,Ltd.Rights Reserved
 * @Company     : deepthink.ai
 * @author      : td
 * @version     : 1.0 
 * @CreateDate  : 2018年9月17日 下午9:36:40
 *
 */
public class SnowUtil {

	protected static Logger logger = LoggerFactory.getLogger(SnowUtil.class);
	
	private final static Map<String,MagicSnowFlake> maps = new ConcurrentHashMap<>();
	private static long ip;
	private static long group;
	static {
	    try {
            InetAddress local = getLocalHostAddress();
            String address = local.getHostAddress();
			String[] addressArray = address.split("\\.");
			//String[] addressArray = StringUtils.split(address,'.');
            ip = Long.parseLong(addressArray[addressArray.length - 1]);
            // 支持线上微卡贷1、2ip段，大王222,39ip段
            group = (Long.parseLong(addressArray[addressArray.length - 2])%6+3)%4;
            logger.info("正常获取ip后面数字{},group为{}", ip, group);
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
            Random random = new Random();
            ip = random.nextInt(256);
            group = random.nextInt(4);
            logger.info("未正常获取ip，获取随机数字{},group为{}", ip, group);
        }
	}
	
	/**
	 * 根据表名获取id，表名不能为空
	 * @param table
	 * @return
	 */
	public static Long getId(String table) {
	    if (!maps.containsKey(table)) {
	        MagicSnowFlake snow = new MagicSnowFlake(ip, group);
	        maps.putIfAbsent(table, snow);
	    }
	    return maps.get(table).nextId();
	}



	private static InetAddress getLocalHostAddress() throws UnknownHostException {
        Enumeration allNetInterfaces;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();

                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        if (ip != null && ip instanceof Inet4Address) {
                            return ip;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            logger.error(e.getMessage(), e);
        }

        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
        if (jdkSuppliedAddress == null) {
            throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
        }
        return jdkSuppliedAddress;
    }


	static class MagicSnowFlake {

	    //其实时间戳   2006-01-01 00:00:00
	    private final static long twepoch = 1163200000000L;

	    //10bit（位）的工作机器id  中IP标识所占的位数 8bit（位）
	    private final static long ipIdBits = 8L;

	    //IP标识最大值 255  即2的8次方减一。
	    private final static long ipIdMax = ~ (-1L << ipIdBits);

	    //10bit（位）的工作机器id  中数字标识id所占的位数 2bit（位）
	    private final static long dataCenterIdBits = 2L;

	    //数字标识id最大值 3  即2的2次方减一。
	    private final static long dataCenterIdMax = ~ (-1L << dataCenterIdBits);

	    //序列在id中占的位数 12bit
	    private final static long seqBits = 12L;

	    //序列最大值 4095 即2的12次方减一。
	    private final static long seqMax = ~(-1L << seqBits);

	    // 64位的数字：首位0  随后41位表示时间戳 随后10位工作机器id（8位IP标识 + 2位数字标识） 最后12位序列号
	    private final static long dataCenterIdLeftShift = seqBits;
	    private final static long ipIdLeftShift = seqBits + dataCenterIdBits;
	    private final static long timeLeftShift = seqBits  + dataCenterIdBits + ipIdBits;

	    //IP标识(0~255)
	    private long ipId;

	    // 数据中心ID(0~3)
	    private long dataCenterId;

	    // 毫秒内序列(0~4095)
	    private long seq = 0L;

	    // 上次生成ID的时间截
	    private long lastTime = -1L;

	    public MagicSnowFlake(long ipId, long dataCenterId) {
	        if(ipId < 0 || ipId > ipIdMax) {
	            logger.error(" ---------- ipId不在正常范围内(0~"+ipIdMax +") " + ipId);
	            throw new ApiException(ErrorCode.SYSTEM_ERROR, "ipId不在正常范围内");
	        }

	        if(dataCenterId < 0 || dataCenterId > dataCenterIdMax) {
	            logger.error(" ---------- dataCenterId不在正常范围内(0~"+dataCenterIdMax +") " + dataCenterId);
	            throw new ApiException(ErrorCode.SYSTEM_ERROR, "dataCenterId不在正常范围内");
	        }

	        this.ipId = ipId;
	        this.dataCenterId = dataCenterId;
	    }

	    public synchronized Long nextId() {
	        long nowTime = System.currentTimeMillis();

	        if(nowTime < lastTime) {
	            logger.error("当前时间小于记录时间");
	            throw new ApiException(ErrorCode.SYSTEM_ERROR, "当前时间小于记录时间");
	        }

	        if(nowTime == lastTime) {
	            seq = (seq + 1) & seqMax;
	            if(seq == 0) {
	                nowTime = getNextTimeStamp();
	            }
	        } else {
	            seq = 0L;
	        }

	        lastTime = nowTime;

	        return ((nowTime - twepoch) << timeLeftShift)
	                | (ipId << ipIdLeftShift)
	                | (dataCenterId << dataCenterIdLeftShift)
	                | seq;
	    }

	    private long getNextTimeStamp() {
	        long nowTime;
	        do {
	            nowTime = System.currentTimeMillis();
	        } while(nowTime <= lastTime);
	        return nowTime;
	    }
	}
}
