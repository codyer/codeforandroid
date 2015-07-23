package com.pao123.contrl.startrun;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.pao123.www.R;
import com.pao123.www.model.WorkOutManager;

public class FragmentRunningMapView extends Fragment implements LocationSource,
		AMapLocationListener {

	private static MapFragment fragment = null;
	public static final int POSITION = 0;

	private MapView mapView;
	private AMap aMap;
	private View mapLayout;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private Marker marker;// 定位雷达小图标
	private TextView debugText;

	public static MapFragment newInstance() {
		if (fragment == null) {
			synchronized (MapFragment.class) {
				if (fragment == null) {
					fragment = new MapFragment();
				}
			}
		}
		return fragment;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mapLayout == null) {
//			Log.i("sys", "MF onCreateView() null");
			mapLayout = inflater.inflate(R.layout.fragment_map_view, null);
			mapView = (MapView) mapLayout.findViewById(R.id.running_map_view);
			debugText = (TextView) mapLayout.findViewById(R.id.debug_text);
			mapView.onCreate(savedInstanceState);
			init();
			// if (aMap == null) {
			// aMap = mapView.getMap();
			// }
		} else {
			if (mapLayout.getParent() != null) {
				((ViewGroup) mapLayout.getParent()).removeView(mapLayout);
			}
		}
		return mapLayout;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// ((MainActivity) activity).onSectionAttached(Constants.MAP_FRAGMENT);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 初始化
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
		marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icons(giflist).period(50));
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		// myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.TRANSPARENT);// 设置圆形的填充颜色
		// myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));//
		// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setMyLocationRotateAngle(180);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
//		Log.e("locationManager", "onResume");
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
//		deactivate();
//		Log.e("locationManager", "onPause");
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		deactivate();
//		Log.e("locationManager", "onDestroy");
	}

	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		Log.e("locationManager", "onLocationChanged");
		if (mListener != null && aLocation != null && aLocation.getTime() > 0) {
			Double geoLat = aLocation.getLatitude();
			Double geoLng = aLocation.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = aLocation.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
					+ "\n精    度    :" + aLocation.getAccuracy() + "米"
					+ "\n定位方式:" + aLocation.getProvider() + "\n定位时间:"
					+ convertToTime(aLocation.getTime()) +"\n定位时间long："+aLocation.getTime()+ "\n城市编码:"
					+ cityCode + "\n位置描述:" + desc + "\n省:"
					+ aLocation.getProvince() + "\n市:" + aLocation.getCity()
					+ "\n区(县):" + aLocation.getDistrict() + "\n区域编码:" + aLocation
					.getAdCode());
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			marker.setPosition(new LatLng(aLocation.getLatitude(), aLocation
					.getLongitude()));// 定位雷达小图标
			float bearing = aMap.getCameraPosition().bearing;
			aMap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度

			// aMap.moveCamera(CameraUpdateFactory.zoomTo(4));
			aMap.setMapTextZIndex(2);
			if (WorkOutManager.getInstance().getStartLocation() == null) {
//				Toast.makeText(getActivity(), "start", Toast.LENGTH_LONG)
//						.show();
				// 绘制一条带有纹理的直线
//				aMap.addPolyline((new PolylineOptions())
//						.add(new LatLng(29.654, 91.139),
//								new LatLng(22.265, 114.188)).setCustomTexture(
//								BitmapDescriptorFactory.defaultMarker()));

				debugText.setText(str);
				WorkOutManager.getInstance().setStartLocation(aLocation);
				drawStartMaker(new LatLng(aLocation.getLatitude(),
						aLocation.getLongitude()));
			} else {
//				Toast.makeText(
//						getActivity(),
//						"changed to:" + aLocation.getLatitude() + ","
//								+ aLocation.getLongitude(), Toast.LENGTH_SHORT)
//						.show();
				debugText.setText(str +
						"\nlastloc:("+WorkOutManager.getInstance()
						.getLastLocation().getLatitude()+","+ WorkOutManager.getInstance()
						.getLastLocation()
						.getLongitude()+")"
						);
				aMap.addPolyline((new PolylineOptions())
						.add(new LatLng(WorkOutManager.getInstance()
								.getLastLocation().getLatitude(),
								WorkOutManager.getInstance()
										.getLastLocation().getLongitude()),
								new LatLng(aLocation.getLatitude(), aLocation
										.getLongitude())).width(10)
						.geodesic(true)
						.color(Color.RED));
			}
			WorkOutManager.getInstance().setLastLocation(aLocation);
		//	Log.e("locationManager", "locationManager num++++++++ ");
		}
	}
	/**
	 * long类型时间格式化
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}
	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy
					.getInstance(getActivity());
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);

//			Log.e("locationManager", "activate");
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
//		Log.e("locationManager", "deactivate ");
	}

	private void drawStartMaker(LatLng startlatlng) {
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

		MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 0.5f)
				.position(startlatlng).title("起点").snippet("跑步起点")
				.icons(giflist).draggable(true).period(50);
		ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
		markerOptionlst.add(markerOption);
		aMap.addMarkers(markerOptionlst, true);
	}

}