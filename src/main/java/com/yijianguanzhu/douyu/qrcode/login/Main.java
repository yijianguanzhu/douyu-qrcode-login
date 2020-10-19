/**
 * 
 */
package com.yijianguanzhu.douyu.qrcode.login;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.apache.http.entity.ContentType;

import com.yijianguanzhu.douyu.qrcode.login.core.DouyuLogin;
import com.yijianguanzhu.douyu.qrcode.login.core.RefreshCookie;
import com.yijianguanzhu.douyu.qrcode.login.core.model.QRCode;
import com.yijianguanzhu.douyu.qrcode.login.enums.Mode;
import com.yijianguanzhu.douyu.qrcode.login.http.Http;
import com.yijianguanzhu.douyu.qrcode.login.utils.CookieUtil;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月17日
 *
 */
public class Main {

	public static void main( String[] args ) throws InterruptedException, ExecutionException {
		// System.setProperty( "org.slf4j.simpleLogger.defaultLogLevel", "debug" );
		jackpot();
	}

	public static void login() {
		QRCode<String> qrCode = DouyuLogin.getBase64QRCode();
		System.out.println( qrCode.getQrCode() );
		Map<String, String> cookie = DouyuLogin.getDouyuCookie( qrCode.getTtl(), qrCode.getCode() );
		System.out.println( CookieUtil.map2CookieString( cookie ) );
		Map<String, String> refresh = RefreshCookie.refresh( cookie );
		System.out.println( CookieUtil.map2CookieString( refresh ) );
	}

	public static void exchangeGift() {
		Timer timer = new Timer();
		timer.schedule( new TimerTask() {

			@Override
			public void run() {
				boolean exchangeGift = exchangeGift0();
				if ( !exchangeGift ) {
					System.out.println( "积分不足" );
					this.cancel();
					timer.cancel();
				}
			}

		}, 500L, 1L );
	}

	public static boolean exchangeGift0() {
		final String url = "https://www.douyu.com/japi/carnival/nc/point/exchangeGift";

		Map<String, String> headers = new LinkedHashMap<String, String>();
		headers.put( "User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36" );
		headers.put( "Referer", "https://www.douyu.com/topic/yngs" );
		headers.put( "Cookie",
				"PHPSESSID=v8uf8bkj350edb5r6l9dil7714;acf_auth=85b8G5KAa46%2BGUuO2XVYTPGM0diGSRMIlsc25py%2Fv%2BlzQDPhuEXLKZGIcjFq%2Fkub6L2HUrc3%2BoSntncFccUpOMU4tpuN3eD8UB2WY3Za8%2BGL2dyL0bDiQmfb;dy_auth=19ea241HbdpEWP1wz1tWDLIr%2FhHVLDaa66u3AhTGMWBF65X1Cc2HPWt4ubj1Pwgl5ErrpXBebY7U6p%2FhYUhgOAlQd%2FW4u9qfWSQFWm6CvBx1SA2NtZJm8CBC;wan_auth37wan=111032636349DySSNDmyMxz5PhTFFyTYcdsvLfW788qyTdeW2kMjqTJc4l9Iprhwdj9oUtnT%2FRMRn%2F36BdDnhV8lyotwCA7EdAi6A1Pl1gLnHrjcSw;acf_uid=16990772;acf_username=qq_WPMcVXmF;acf_nickname=%E6%88%91%E5%97%85%E5%88%B0%E4%BA%86%E6%87%A6%E5%A4%AB%E7%9A%84%E5%91%B3%E9%81%93;acf_own_room=0;acf_groupid=1;acf_phonestatus=1;acf_avatar=https%3A%2F%2Fapic.douyucdn.cn%2Fupload%2Favatar_v3%2F202002%2F537ee7d220ad4b119efc4cfdf3f211a5_;acf_ct=0;acf_ltkid=97100706;acf_biz=1;acf_stk=d8bbe5dd15fe1d14;acf_devid=9711e23d561edb9ba58861153ba60a43;last_login_way=scan;LTP0=e16e3f1CtaOYZ8IyUCdV4d7Mr7puoJBpzvLTVdArLIn1pBREIIM7s1O1QPSGPQHlZuNeWnP2mHNC1FAEcuLYXeImwes4CmS6CPKUmJWFwFNOpw6lIXQBgbpGNhEn3MDzHeuJ5%2BoPNxXKjHS0oXpyNjWKKentrq98G5rdorONU1Y6KSoenWUUu7UqAWZZ0PUOEn3FCo;cvl_csrf_token=4df7916ecd5441328d3122e94df25e54; " );
		Map<String, Object> body = new LinkedHashMap<String, Object>();
		body.put( "nameEn", "20201015s10vodwatch_credits1" );
		body.put( "giftId", "1197" );
		body.put( "csrfToken", "4df7916ecd5441328d3122e94df25e54" );
		body.put( "isNewRisk", 1 );

		Map<?, ?> map = Http.getHttpResults( url, headers, Map.class, body, Mode.POST,
				ContentType.APPLICATION_FORM_URLENCODED );
		System.out.println( map );
		return ( int ) map.get( "error" ) == 0;
	}

	public static void jackpot() {
		Timer timer = new Timer();
		timer.schedule( new TimerTask() {

			@Override
			public void run() {
				boolean jackpot = jackpot0();
				if ( !jackpot ) {
					System.out.println( "抽奖次数不足" );
					this.cancel();
					timer.cancel();
				}
			}

		}, 500L, 20L );
	}

	public static boolean jackpot0() {
		final String url = "https://www.douyu.com/japi/carnival/nc/lottery/jackpot";

		Map<String, String> headers = new LinkedHashMap<String, String>();
		headers.put( "User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36" );
		headers.put( "Referer", "https://www.douyu.com/topic/yngs" );
		headers.put( "Content-Type", "application/json" );
		headers.put( "Cookie",
				"PHPSESSID=v8uf8bkj350edb5r6l9dil7714;acf_auth=85b8G5KAa46%2BGUuO2XVYTPGM0diGSRMIlsc25py%2Fv%2BlzQDPhuEXLKZGIcjFq%2Fkub6L2HUrc3%2BoSntncFccUpOMU4tpuN3eD8UB2WY3Za8%2BGL2dyL0bDiQmfb;dy_auth=19ea241HbdpEWP1wz1tWDLIr%2FhHVLDaa66u3AhTGMWBF65X1Cc2HPWt4ubj1Pwgl5ErrpXBebY7U6p%2FhYUhgOAlQd%2FW4u9qfWSQFWm6CvBx1SA2NtZJm8CBC;wan_auth37wan=111032636349DySSNDmyMxz5PhTFFyTYcdsvLfW788qyTdeW2kMjqTJc4l9Iprhwdj9oUtnT%2FRMRn%2F36BdDnhV8lyotwCA7EdAi6A1Pl1gLnHrjcSw;acf_uid=16990772;acf_username=qq_WPMcVXmF;acf_nickname=%E6%88%91%E5%97%85%E5%88%B0%E4%BA%86%E6%87%A6%E5%A4%AB%E7%9A%84%E5%91%B3%E9%81%93;acf_own_room=0;acf_groupid=1;acf_phonestatus=1;acf_avatar=https%3A%2F%2Fapic.douyucdn.cn%2Fupload%2Favatar_v3%2F202002%2F537ee7d220ad4b119efc4cfdf3f211a5_;acf_ct=0;acf_ltkid=97100706;acf_biz=1;acf_stk=d8bbe5dd15fe1d14;acf_devid=9711e23d561edb9ba58861153ba60a43;last_login_way=scan;LTP0=e16e3f1CtaOYZ8IyUCdV4d7Mr7puoJBpzvLTVdArLIn1pBREIIM7s1O1QPSGPQHlZuNeWnP2mHNC1FAEcuLYXeImwes4CmS6CPKUmJWFwFNOpw6lIXQBgbpGNhEn3MDzHeuJ5%2BoPNxXKjHS0oXpyNjWKKentrq98G5rdorONU1Y6KSoenWUUu7UqAWZZ0PUOEn3FCo;cvl_csrf_token=4df7916ecd5441328d3122e94df25e54; " );
		Map<String, Object> body = new LinkedHashMap<String, Object>();
		body.put( "activityId", "565" );
		body.put( "csrfToken", "4df7916ecd5441328d3122e94df25e54" );

		Map<?, ?> map = Http.getHttpResults( url, headers, Map.class, body, Mode.POST,
				ContentType.APPLICATION_JSON );
		System.out.println( map );
		return ( int ) map.get( "error" ) == 0;
	}
}
