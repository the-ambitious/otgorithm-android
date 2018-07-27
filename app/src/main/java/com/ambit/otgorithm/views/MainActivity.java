package com.ambit.otgorithm.views;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.BannerViewPagerAdapter;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private long pressedTime = 0;

    // 툴바 변수 선언
    private DrawerLayout mDrawerLayout;

    // firebase 인증 객체 싱글톤으로 어느 곳에서나 부를수 있다.웹에서 세션개념과 비슷하다.
    private FirebaseAuth mAuth;

    // 변수 선언
    public static int TO_GRID = 0;
    public static int TO_GPS = 1;
    static int rain;                  //POP  강수확률

    static int snowrain;              //강수형태(PTY) 코드 : 없음(0), 비(1), 비/눈(2), 눈(3)
    // 여기서 비/눈은 비와 눈이 섞여 오는 것을 의미 (진눈개비)

    static int humidity;              //REH  습도
    static int precipitation;         //R06  6시간 강수량
    //String lowtemper;        //TMN  아침 최저기온
    //String hightemper;       //TMX  낮 최고기온
    static int currenttemper;         //T3H  3시간 기온
    static int windspeed;             //WSD  풍속
    static int sky;                     // 하늘상태(SKY) 코드 : 맑음(1), 구름조금(2), 구름많음(3), 흐림(4)
    static int weather_Icon;

    LocationManager lm = null;
    ViewPager viewPager;
    BannerViewPagerAdapter bannerViewPagerAdapter;
    ArrayList arrayList; // viewPager에서 보여줄 item

    //날씨 아이콘
    ImageView weathericon;
    //날씨 설명
    TextView weatherdiscrip;
    //온도
    TextView temper;

    private double nx;
    private double ny;

    // 현재 날짜와 시간을 초기화하는 부분
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd/HHmm");
    String formatDate = sdfNow.format(date);
    String[] rightNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        SystemClock.sleep(2000);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase 인증객체 얻기
        mAuth = FirebaseAuth.getInstance();

        // 도움말; a는 마치 File 이름
        SharedPreferences preference = getSharedPreferences("a", MODE_PRIVATE);
        int firstviewshow = preference.getInt("First", 0);

        // 도움말 첫 실행 여부를 저장하는 SharedPreferences 값인 firstviewshow가 1이 아니면 실행
        if (firstviewshow != 1) {
            Intent intent = new Intent(this, FirstStartActivity.class);
            startActivity(intent);
        }

        // 툴바 부분
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                Intent intent;
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_item_closet:
                        intent = new Intent(MainActivity.this, SignInActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_item_favorites:
                        intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_item_letterbox:
                        Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.nav_contact_notice:
                        Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.nav_contact_commentary:
                        intent = new Intent(MainActivity.this, UploadPicture.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_aboutUs_logout:
                        Log.v("알림", "LOGOUT 아이템 클릭");
                        AlertDialog.Builder alt_bld = new AlertDialog.Builder(MainActivity.this);
                        alt_bld.setTitle("종료")
                                .setMessage("로그아웃 하시겠습니까?")
                                .setCancelable(false)
                                .setPositiveButton("네",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // 네 클릭
                                                // 로그아웃 함수 call
                                                mAuth.signOut();
                                                LoginManager.getInstance().logOut();
                                                finish();
                                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                Log.v("알림", "페북로그아웃 LOGOUT");
                                                startActivity(intent);
                                            }
                                        }).setNegativeButton("아니오",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // 아니오 클릭. dialog 닫기.
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = alt_bld.create();
                        alt_bld.show();
                        break;
                }
                return true;
            }
        });


        // strings.xml
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MessageFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_message);
        }*/
/*
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
*/



        //arrayList에 메인화면에 띄워지는 사진3장 담기
        arrayList = new ArrayList();
        for (int i = 0; i < 3; i++) {
            arrayList.add(R.drawable.ad1 + i);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPagerAdapter에 사진을 담은 arrayList 전달
        bannerViewPagerAdapter = new BannerViewPagerAdapter(getLayoutInflater(), arrayList,this);
        //adapter를 통한 viewpage 설정
        viewPager.setAdapter(bannerViewPagerAdapter);

        //메인 날씨띄우는 부분에 있는 view들 불러오기 이미지,온도.아이콘
        weathericon = (ImageView) findViewById(R.id.weathericon);
        weatherdiscrip = (TextView) findViewById(R.id.weatherdiscrip);
        temper = (TextView) findViewById(R.id.temper);



        /**
            sdk23을 기준으로 허가권을 얻는 방식이 바뀜
            23미만 버전에서는 manifest에 permission추가하는 것으로
            끝이었지만 23이상부터는 앱이 돌아가는 중에 허가권을
            매번 요청해야함.
        */
        if (Build.VERSION.SDK_INT >= 23) {
            //위치정보를 불러오기위한 허가권
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //허가권이 없을 경우 허가권을 요청해야함.
                //requestPermissions()함수를 호출하면 아래에 onRequestPermissionsResult()메소드가 호출됨.
                //requestPermissions(activity, 허가권 배열, 요청번호)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                //허가권이 있을 경우 바로 위치를 찾는 함수 호출.
                findLocation();
            }
        } else {
            //23버전 미만일 경우에는 바로 허가권요청없이 위치찾기 시작.
            findLocation();
        }

    }   // end of onCreate()

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 한번을 눌렀을 경우 " 한 번 더 누르면 종료됩니다. " 라는 안내 Toast를 띄우도록 하고
     * 두번째 뒤로가기 버튼을 누른 시간이 2초가 넘어간다면 pressedTime을 초기화하고 Toast를 띄운다.
     * 2초가 넘지않는다면 app을 종료시키는 액션을 수행할 수 있다.
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        if ( pressedTime == 0 ) {
            Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if (seconds > 2000) {
                Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
                pressedTime = 0;
            } else {
                super.onBackPressed();
//                finish(); // app 종료 시키기
            }
        }
    }

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button1:
                intent = new Intent(this, SortieActivity.class);
                break;
            case R.id.button2:
                intent = new Intent(this, GalleryActivity.class);
                break;
            case R.id.button3:
                intent = new Intent(this, ProvinceActivity.class);
                break;
        }
        startActivity(intent);
    }

    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).commit();
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChattingFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "send", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/   // end of

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/


    public void weatherInfo(double nx, double ny) {
        // 지정한 포맷을 / 으로 split; rightNow[0]에는 년/월/일이 담김
        // rightNow[1]에는 현재 시간과 분이 담김
        rightNow = formatDate.split("/");

        int time = Integer.parseInt(rightNow[1]);

        if(time>=0 && time <210){
            int a=Integer.parseInt(rightNow[0]);
            rightNow[0] = Integer.toString(a-1);
            rightNow[1] = "2000";
        } else if (time >= 210 && time < 510) {
            Log.d("12시 15분 테스트", "첫 if문 진입");
            int a=Integer.parseInt(rightNow[0]);
            rightNow[0] = Integer.toString(a-1);
            rightNow[1] = "2300";
        } else if (time >= 510 && time < 810) {
            rightNow[1] = "0200";
        } else if (time >= 810 && time < 1110) {
            rightNow[1] = "0500";
        } else if (time >= 1110 && time < 1410) {
            rightNow[1] = "0800";
        } else if (time >= 1410 && time < 1710) {
            rightNow[1] = "1100";
        } else if (time >= 1710 && time < 2010) {
            rightNow[1] = "1400";
        } else if (time >= 2010 && time < 2310) {
            rightNow[1] = "1700";
        } else if(time>=2310){
            Log.d("12시 15분 테스트", "else문 진입");
            rightNow[1] = "2000";
        }

        //예보 발표날짜
        //String baseDate = "20180709";
        //예보 발표시간  Base_time  : 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300 (1일 8회)
        //String baseTime = "1400";
        //서비스 인증키
        String serviceKey = "J6pxxHVvdyC6qOrOiPtuVTeYHpGwRR3dSIexEj49E6X0IrB20wC%2BC5sJaFNpWsQPRtLs62xy8htWiSFsWIe3YQ%3D%3D";

        //정보를 모아서 URL정보 만듬
        String urlStr = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?" +
                "serviceKey=" + serviceKey +
                "&base_date=" + rightNow[0] +
                "&base_time=" + rightNow[1] +
                "&nx=" + (int) nx +
                "&ny=" + (int) ny +
                "&_type=json";

        new WeatherParsing().execute(urlStr);
    }

    public void findLocation() {
        // LocationManager 객체를 얻어온다
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {

            weatherdiscrip.setText("날씨를 받아오고 있습니다." + "\n" + "잠시만 기다려주세요 : )");

            // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    this);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    this);

                      /*  tv.setText("위치정보 미수신중");
                        lm.removeUpdates(this);  //  미수신할때는 반드시 자원해체를 해주어야 한다.*/

        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //여기서 위치값이 갱신되면 이벤트가 발생한다.
        //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

        Log.d("test", "onLocationChanged, location:" + location);
        double longitude = location.getLongitude(); //경도
        double latitude = location.getLatitude();   //위도
        //double altitude = location.getAltitude();   //고도
        //float accuracy = location.getAccuracy();    //정확도
        //String provider = location.getProvider();   //위치제공자
        //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
        //Network 위치제공자에 의한 위치변화
        //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.

        //위도 경도를 x,y좌표로 변환한 값을 리턴받는다
        LatXLngY tmp = convertGRID_GPS(TO_GRID, latitude, longitude);

        //x,y좌표를 들고 날씨정보 호출하는 함수로 간다.
        weatherInfo(tmp.x, tmp.y);



     /*   infomation.setText("위치정보 : " + provider + "\n위도 : " + tmp.x + "\n경도 : " + tmp.y
                + "\n고도 : " + altitude + "\n정확도 : " + accuracy);*/
        lm.removeUpdates(this);     // 배터리가 소모되므로 manager를 해제해줘야 함
    }

    public double getNx() {
        return nx;
    }

    public void setNx(double nx) {
        this.nx = nx;
    }

    public double getNy() {
        return ny;
    }

    public void setNy(double ny) {
        this.ny = ny;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                //요청한 권한에 대한 결과값이 grantResults[]에 담김
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //권한을 허용한 경우 위치찾기 시작
                    findLocation();
                } else {
                    //허용하지 않은 경우 설정할 텍스트메시지.
                    weatherdiscrip.setText("위치정보를 허용하셔야 합니다.");
                }
                break;
        }


    }


    /**
     * convertGRID_GPS() : 위도, 경도를 x, y 좌표로 변환시켜주는 메서드
     * @param mode
     * @param lat_X
     * @param lng_Y
     * @return
     */
    private LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y) {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //



        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        if (mode == TO_GRID) {
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        } else {
            rs.x = lat_X;
            rs.y = lng_Y;
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            } else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                } else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }
        return rs;
    }

    class LatXLngY {
        public double lat;
        public double lng;

        public double x;
        public double y;

    }

    class WeatherParsing extends AsyncTask<String, Void, StringBuffer> {

  /*      int rain;                  //POP  강수확률
        int snowrain;              //PTY  없음(0), 비(1), 비/눈(2), 눈(3)
        int humidity;              //REH  습도
        int precipitation;         //R06  6시간 강수량
        //String lowtemper;        //TMN  아침 최저기온
        //String hightemper;       //TMX  낮 최고기온
        int currenttemper;         //T3H  3시간 기온
        int windspeed;             //WSD  풍속
        int sky;                   //SKY  맑음(1), 구름조금(2), 구름많음(3), 흐림(4)*/


        @Override
        protected void onPostExecute(StringBuffer buffer) {

            String json = buffer.toString();
            try {
                Log.d("ㅁㅁㅁㅁ", json);

                JSONArray array = new JSONObject(json).getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String category = obj.getString("category");
                    switch (category) {
                        case "POP":
                            rain = obj.getInt("fcstValue");
                            break;
                        case "PTY":
                            snowrain = obj.getInt("fcstValue");
                            break;
                        case "REH":
                            humidity = obj.getInt("fcstValue");
                            break;
                        case "R06":
                            precipitation = obj.getInt("fcstValue");
                            break;
                     /*   case "TMN":
                            break;
                        case "TMX":
                            break;*/
                        case "T3H":
                            currenttemper = obj.getInt("fcstValue");
                            break;
                        case "WSD":
                            windspeed = obj.getInt("fcstValue");
                            break;
                        case "SKY":
                            sky = obj.getInt("fcstValue");
                            break;
                    }
                }

                // snowrain : 비, 눈 알려주는 것
                switch (snowrain) {
                        case 1:
                            weather_Icon = R.drawable.rain;
                            weathericon.setImageResource(R.drawable.rain);
                            weatherdiscrip.setText("비가 오네요");
                            temper.setText(currenttemper + "도");
                            break;
                        case 2:
                            weather_Icon = R.drawable.snowrain;
                            weathericon.setImageResource(R.drawable.snowrain);
                            weatherdiscrip.setText("진눈개비가 내리네요");
                            temper.setText(currenttemper + "도");
                            break;
                        case 3:
                            weather_Icon = R.drawable.snow;
                            weathericon.setImageResource(R.drawable.snow);
                            weatherdiscrip.setText("눈이 내립니다");
                            temper.setText(currenttemper + "도");
                            break;

                    default:
                        break;
                }

                if (sky == 1) {
                    weather_Icon = R.drawable.sunny;
                    weathericon.setImageResource(R.drawable.sunny);
                    weatherdiscrip.setText("날이 맑네요");
                    temper.setText(currenttemper + "도");
                } else if (sky == 2 || sky == 3) {
                    weather_Icon = R.drawable.cloudy;
                    weathericon.setImageResource(R.drawable.cloudy);
                    weatherdiscrip.setText("구름이 있네요");
                    temper.setText(currenttemper + "도");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        protected StringBuffer doInBackground(String... urls) {
            try {
                // urlStr을 이용해서 URL 객체를 만든다.
                URL url = new URL(urls[0]);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = br.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Toast.makeText(this,"현재 회원 : "+currentUser.getEmail(),Toast.LENGTH_SHORT).show();

            Log.d("이메일",currentUser.getEmail());
            Log.d("유아이디",currentUser.getUid());
            //Log.d("폰넘버",currentUser.getPhoneNumber());
            Log.d("디스플레이네임",currentUser.getDisplayName());
            Log.d("프로바이더아이디",currentUser.getProviderId());
            Log.d("포토 유알엘",currentUser.getPhotoUrl().toString());
           /* mAuth.getCurrentUser().getPhotoUrl();
            mAuth.getCurrentUser().getEmail();
            mAuth.getCurrentUser().getUid();
            mAuth.getCurrentUser().getPhoneNumber();
            mAuth.getCurrentUser().getDisplayName();
            mAuth.getCurrentUser().getProviderId();*/

        }else {
            Toast.makeText(this,"현재 회원 : 없음",Toast.LENGTH_SHORT).show();
        }
        //updateUI(currentUser);
    }


}