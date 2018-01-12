package com.richinfo.common.utils;

/**
 * Copyright (c) 2010, 新浪网支付中心
 * 		All rights reserved.
 * 
 * Java IP地址字符串与BigInteger的转换,
 * 支持IPv6
 * 
 */

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * IP转换大数进行比较工具
 * 
 * @author yuchao1@staff.sina.com.cn
 * @since 2011-03-17
 * 
 */
public final class IpLimit {
	// 存放IP范围的内引类
	class IpRange {
		private String[][] ipRange;

		public IpRange(String[][] ip) {
			this.ipRange = ip;
		}

		public String getIpAt(int row, int column) {
			return ipRange[row][column];
		}

	}

	/**
	 * 防止被实例化
	 */
	private IpLimit() {
	}

	/**
	 * 将字符串形式的ip地址转换为BigInteger
	 * 
	 * @param ipInString
	 *            字符串形式的ip地址
	 * @return 整数形式的ip地址
	 */
	public static BigInteger StringToBigInt(String ipInString) {
		ipInString = ipInString.replace(" ", "");
		byte[] bytes;
		if (ipInString.contains(":"))
			bytes = ipv6ToBytes(ipInString);
		else
			bytes = ipv4ToBytes(ipInString);
		return new BigInteger(bytes);
	}

	/**
	 * 将整数形式的ip地址转换为字符串形式
	 * 
	 * @param ipInBigInt
	 *            整数形式的ip地址
	 * @return 字符串形式的ip地址
	 */
	public static String BigIntToString(BigInteger ipInBigInt) {
		byte[] bytes = ipInBigInt.toByteArray();
		byte[] unsignedBytes = bytes;

		// 去除符号位
		try {
			String ip = InetAddress.getByAddress(unsignedBytes).toString();
			return ip.substring(ip.indexOf('/') + 1).trim();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ipv6地址转有符号byte[17]
	 * 
	 * @param ipv6
	 *            字符串形式的IP地址
	 * @return big integer number
	 */
	private static byte[] ipv6ToBytes(String ipv6) {
		byte[] ret = new byte[17];
		ret[0] = 0;
		int ib = 16;
		boolean comFlag = false;// ipv4混合模式标记
		if (ipv6.startsWith(":"))// 去掉开头的冒号
			ipv6 = ipv6.substring(1);
		String groups[] = ipv6.split(":");
		for (int ig = groups.length - 1; ig > -1; ig--) {// 反向扫描
			if (groups[ig].contains(".")) {
				// 出现ipv4混合模式
				byte[] temp = ipv4ToBytes(groups[ig]);
				ret[ib--] = temp[4];
				ret[ib--] = temp[3];
				ret[ib--] = temp[2];
				ret[ib--] = temp[1];
				comFlag = true;
			} else if ("".equals(groups[ig])) {
				// 出现零长度压缩,计算缺少的组数
				int zlg = 9 - (groups.length + (comFlag ? 1 : 0));
				while (zlg-- > 0) {// 将这些组置0
					ret[ib--] = 0;
					ret[ib--] = 0;
				}
			} else {
				int temp = Integer.parseInt(groups[ig], 16);
				ret[ib--] = (byte) temp;
				ret[ib--] = (byte) (temp >> 8);
			}
		}
		return ret;
	}

	/**
	 * ipv4地址转有符号byte[5]
	 * 
	 * @param ipv4
	 *            字符串的IPV4地址
	 * @return big integer number
	 */
	private static byte[] ipv4ToBytes(String ipv4) {
		byte[] ret = new byte[5];
		ret[0] = 0;
		// 先找到IP地址字符串中.的位置
		int position1 = ipv4.indexOf(".");
		int position2 = ipv4.indexOf(".", position1 + 1);
		int position3 = ipv4.indexOf(".", position2 + 1);
		// 将每个.之间的字符串转换成整型
		ret[1] = (byte) Integer.parseInt(ipv4.substring(0, position1));
		ret[2] = (byte) Integer.parseInt(ipv4.substring(position1 + 1,
				position2));
		ret[3] = (byte) Integer.parseInt(ipv4.substring(position2 + 1,
				position3));
		ret[4] = (byte) Integer.parseInt(ipv4.substring(position3 + 1));
		return ret;
	}

	/**
	 * 
	 * @param tip
	 *            要限制的Ip 包括Ipv6
	 * @param sip
	 *            限制的开始Ip
	 * @param eip
	 *            限制的结束Ip
	 * @return Boolean true通过 false 受限制
	 */
	public static boolean IsIp(String tip, String[][] myRange) {
		boolean flag = false;
		// tbig 要测试的大数
		BigInteger tbig = IpLimit.StringToBigInt(tip);
		int rangeLength = myRange.length;

		for (int i = 0; i < rangeLength; i++) {
			for (int j = 0; j < myRange[i].length; j++) {
				// 开始大数sbig和ebig
				BigInteger sbig = IpLimit.StringToBigInt(myRange[i][j]);
				j = j + 1;
				BigInteger ebig = IpLimit.StringToBigInt(myRange[i][j]);
				// 将大数进行比较
				// 如果相等则退出循环
				if ((tbig.compareTo(sbig)) == 0) {
					flag = true;
					break;
				}
				// 如果不相等则比较大小，在区间内正常
				if (((tbig.compareTo(sbig)) == 1)
						&& ((tbig.compareTo(ebig)) == -1)) {
					flag = true;
					break;
				}

			}
		}
		return flag;
	}

	// IPv6 address validator matches these IPv6 formats
	// ::ffff:21:7.8.9.221 | 2001:0db8:85a3:08d3:1319:8a2e:0370:7344
	// | ::8a2e:0:0370:7344 | 2001:0db8:85a3:08d3:1319:8a2e:100.22.44.55
	// | 2001:0db8::8a2e:100.22.44.55 | ::100.22.44.55 | ffff::
	// And such addresses are invalid
	// ::8a2e:0:0370:7344.4 | 2001:idb8::111:7.8.9.111 | 2001::100.a2.44.55
	// | :2001::100.22.44.55
	public static boolean isIPV6Format(String ip) {
		ip = ip.trim();

		// in many cases such as URLs, IPv6 addresses are wrapped by []
		if (ip.substring(0, 1).equals("[")
				&& ip.substring(ip.length() - 1).equals("]"))

			ip = ip.substring(1, ip.length() - 1);

		return (1 < Pattern.compile(":").split(ip).length)
		// a valid IPv6 address should contains no less than 1,
		// and no more than 7 “:” as separators
				&& (Pattern.compile(":").split(ip).length <= 8)

				// the address can be compressed, but “::” can appear only once
				&& (Pattern.compile("::").split(ip).length <= 2)

				// if a compressed address
				&& (Pattern.compile("::").split(ip).length == 2)

		// if starts with “::” – leading zeros are compressed
		? (((ip.substring(0, 2).equals("::")) ? Pattern
				.matches(
						"^::([\\da-f]{1,4}(:)){0,4}(([\\da-f]{1,4}(:)[\\da-f]{1,4})|([\\da-f]{1,4})|((\\d{1,3}.){3}\\d{1,3}))",
						ip)
				: Pattern
						.matches(
								"^([\\da-f]{1,4}(:|::)){1,5}(([\\da-f]{1,4}(:|::)[\\da-f]{1,4})|([\\da-f]{1,4})|((\\d{1,3}.){3}\\d{1,3}))",
								ip)))

				// if ends with "::" - ending zeros are compressed
				: ((ip.substring(ip.length() - 2).equals("::")) ? Pattern
						.matches("^([\\da-f]{1,4}(:|::)){1,7}", ip)
						: Pattern
								.matches(
										"^([\\da-f]{1,4}:){6}(([\\da-f]{1,4}:[\\da-f]{1,4})|((\\d{1,3}.){3}\\d{1,3}))",
										ip));
	}

	/**
	 * 测试程序
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		/*
		 * String ip = new String("192.168.0.1"); BigInteger number =
		 * IpLimit.StringToBigInt(ip); String sip = "192.168.10.0"; String eip =
		 * "192.168.10.255"; System.out.println("The number ="+number); boolean
		 * flag = false; flag = IpLimit.IsIp(ip, sip, eip);
		 * System.out.println("The int n="+flag);
		 * //System.out.println("the m ="+m);
		 * 
		 * //String myip = IpLimit.BigIntToString(number);
		 * //System.out.println("The ipv4 ="+myip); String ip6 =
		 * "e80:a5eb:8fc8::7ec6:8027"; //String ip6 = "21DA:D3:AD:2F3B::9C5A";
		 * BigInteger num = IpLimit.StringToBigInt(ip6);
		 * System.out.println("The Ipv6 number ="+num); String myip6 =
		 * IpLimit.BigIntToString(num); System.out.println("The IPv6 ="+myip6);
		 */

		// 单IP限制必须写全两个
		String[][] iplimit1 = { { "192.168.0.1", "192.168.0.255" },
				{ "10.210.71.0", "10.210.71.255" },
				{ "202.106.182.158", "202.106.182.158" } };
		boolean flag = false;
		String ip = "202.106.182.158";
		flag = IpLimit.IsIp(ip, iplimit1);
		System.out.println("The first test =" + flag);

		String ip1 = "201.101.102.21";
		flag = IpLimit.IsIp(ip1, iplimit1);
		System.out.println("The other test =" + flag);
	}

	public static String intToIpV4(int i) {
		if(i == 1){
			return LoopbackStringIpV4;//"127.0.0.1";
		}
		
		return ((i >> 24) & 0xFF) + "." +
		((i >> 16) & 0xFF) + "." +
		((i >> 8) & 0xFF) + "." +
		(i & 0xFF);
	}
	
	public static boolean isLoopbackIp(String ip){
		if(!StringUtils.isEmpty(ip)&& (LoopbackStringIpV4.compareToIgnoreCase(ip) == 0
				|| LoopbackStringIpV6Long.compareToIgnoreCase(ip) == 0
				|| LoopbackStringIpV6Short.compareToIgnoreCase(ip) == 0))
			return true;
		return false;
	}
	
	public static String LoopbackStringIpV4 = "127.0.0.1";
	
	public static String LoopbackStringIpV6Long = "0:0:0:0:0:0:0:1";
	
	public static String LoopbackStringIpV6Short = "::1";
}
