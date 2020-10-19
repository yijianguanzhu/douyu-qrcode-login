/**
 * 
 */
package com.yijianguanzhu.douyu.qrcode.login.enums;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

/**
 * @author yijianguanzhu
 *
 * @create 2020年10月16日
 *
 */
public enum Mode {

	GET {
		@Override
		public HttpRequest getRequestMethod( String url ) {
			return new HttpGet( url );
		}
	},

	POST {
		@Override
		public HttpRequest getRequestMethod( String url ) {
			return new HttpPost( url );
		}
	},

	PUT {
		@Override
		public HttpRequest getRequestMethod( String url ) {
			return new HttpPut( url );
		}
	},

	PATCH {
		@Override
		public HttpRequest getRequestMethod( String url ) {
			return new HttpPatch( url );
		}
	},

	DELETE {
		@Override
		public HttpRequest getRequestMethod( String url ) {
			return new HttpDelete( url );
		}
	};

	public abstract HttpRequest getRequestMethod( String url );

}
