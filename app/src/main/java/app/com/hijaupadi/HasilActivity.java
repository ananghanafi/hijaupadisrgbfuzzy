package app.com.hijaupadi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sdsmdg.tastytoast.TastyToast;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class HasilActivity extends AppCompatActivity {
    ImageView imageView;
    Bitmap bitmap;
    TextView tv_1, tv_2, tv_5;
    Button btn_1, btn_2, btn_3;
    ValueLineChart mCubicValueLineChart2;
    ValueLineSeries series2;
    private static final int MAX_INTENSITY = (int) Math.pow(2, 8) - 1;
    BarChart mBarChart, barChartOutput;
    LineChart lineChartR, lineChartG, lineChartB;
    int jml_bin[];
    double jml_bar[];
    int label_bar[];
    int apz1, apz2;
    double [] apr1 = new double[3];
    double [] apr2 = new double[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);
        Intent intent = getIntent();
        imageView = (ImageView) findViewById(R.id.img);
        bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
        //imageView.setImageBitmap(bitmap);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        mBarChart = (BarChart) findViewById(R.id.cubiclinechart);
        barChartOutput = (BarChart) findViewById(R.id.cubiclinechart6);
        mCubicValueLineChart2 = (ValueLineChart) findViewById(R.id.cubiclinechart2);
        lineChartR = (LineChart) findViewById(R.id.cubiclinechart3);
        lineChartG = (LineChart) findViewById(R.id.cubiclinechart4);
        lineChartB = (LineChart) findViewById(R.id.cubiclinechart5);
        if(getIntent().hasExtra("BitmapImage")) {
            bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("BitmapImage"),0,getIntent().getByteArrayExtra("BitmapImage").length);
            imageView.setImageBitmap(bitmap);

        }
        new scan().execute(bitmap);
        SaveImage(bitmap);
    }

    public static int mode(int[] array) {
        HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
        int max = 1, temp = 0;
        for (int i = 0; i < array.length; i++) {
            if (hm.get(array[i]) != null) {
                int count = hm.get(array[i]);
                count = count + 1;
                hm.put(array[i], count);
                if (count > max) {
                    max = count;
                    temp = array[i];
                }
            } else {
                hm.put(array[i], 1);
            }
        }
        return temp;
    }

    public Bitmap resizeBitmap(Bitmap image) {
        image = Bitmap.createScaledBitmap(image, 20, 20, true);
        return image;
    }

    private void SaveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        Log.wtf("FILE : ", "" + file.getAbsolutePath());
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class scan extends AsyncTask<Bitmap, Void, int[]> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(HasilActivity.this);
            dialog.setMessage("Mohon tunggu... ");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected int[] doInBackground(Bitmap... bitmaps) {
            series2 = new ValueLineSeries();
            series2.setColor(0xFFFF0000);
            int width, height;
            Bitmap bmpOriginal = resizeBitmap(bitmaps[0]);
            height = bmpOriginal.getHeight();
            width = bmpOriginal.getWidth();
            int[] red = new int[width * height];
            int[] green = new int[width * height];
            int[] blue = new int[width * height];
            int[] srgb = new int[width * height];
            int[][][] bin = new int[16][3][width * height];
            jml_bin = new int[16];
            jml_bar = new double[2];
            label_bar = new int[2];
            int[] hasil = new int[10];
            List<Integer> srgbl = new ArrayList<Integer>();
            List<Integer> redl = new ArrayList<Integer>();
            List<Integer> bluel = new ArrayList<Integer>();
            List<Integer> greenl = new ArrayList<Integer>();
            double r1 = 0, r2 = 0, g1 = 0, g2 = 0, b1 = 0, b2 = 0;
            int statr1 = 0, statr2 = 0, statg1 = 0, statg2 = 0, statb1 = 0, statb2 = 0, z1 = 0, z2 = 0;
            int j = 0;
            int g = 0;
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    int pixel = bmpOriginal.getPixel(x, y);
                    red[j] = (pixel >> 16) & 0xff;
                    green[j] = (pixel >> 8) & 0xff;
                    blue[j] = (pixel) & 0xff;
                    srgb[j] = red[j] + green[j] + blue[j];
                    if (!srgbl.contains(srgb[j])) {
                        srgbl.add(srgb[j]);
                    }
                    if (!redl.contains(red[j])) {
                        redl.add(red[j]);
                    }
                    if (!bluel.contains(green[j])) {
                        bluel.add(blue[j]);
                    }
                    if (!greenl.contains(green[j])) {
                        greenl.add(green[j]);
                    }

                    double d = srgb[j] / 47.812; //255*3/16 (karena srgb adalah r+g+b. 16 karena panjang bin)
                    int k = (int) d;
                    Log.wtf("" + j, d + " : " + jml_bin[k]);
                    jml_bin[k] += 1;
                    bin[k][0][g] = (pixel >> 16) & 0xff;
                    bin[k][1][g] = (pixel >> 8) & 0xff;
                    bin[k][2][g] = (pixel) & 0xff;
                    j++;
                    g++;
                }
            }
            int[] srgbunique = convertIntegers(srgbl);
            for (int s : srgbunique) {
                series2.addPoint(new ValueLinePoint("" + s, (float) getMaxValue(s, srgb)));
            }
            Log.wtf("arraylength", bin[0][0].length + "");
            hasil[0] = mode(srgb);
            System.out.println("hasil[0] : " +hasil[0]);
            hasil[1] = mode(red);
            System.out.println("hasil[1] : " +hasil[1]);
            hasil[2] = mode(green);
            System.out.println("hasil[2] : " +hasil[2]);
            hasil[3] = mode(blue);
            System.out.println("hasil[3] : " +hasil[3]);
            hasil[4] = 1;
            if (hasil[1] <= 82) {
                //rendah rendah
                r1 = 1;
                r2 = 0;
                statr1 = 1;
                statr2 = 2;
            } else if (hasil[1] > 82 && hasil[1] < 127.5) {
                //rendah sedang
                r1 = (127.5 - hasil[1]) / (127.5 - 82);
                r2 = (hasil[1]) - 82 / (127.5- 82);
                statr1 = 1;
                statr2 = 2;
            } else if (hasil[1] > 127.5 && hasil[1] < 173) {
                //sedang tinggi
                r1 = (173 - hasil[1]) / (173 - 127.5);
                r2 = (hasil[1] - 127.5) / (173 - 127.5);
                statr1 = 2;
                statr2 = 3;
            } else if (hasil[1] >= 173) {
                //
                r1 = 0;
                r2 = 1;
                statr1 = 2;
                statr2 = 3;
            } else  {
                r1 = 1;
                r2 = 1;
                statr1 = 3;
                statr2 = 3;
            }

            if (hasil[2] >= 0 && hasil[2] <= 100) {
                //rendah rendah
                g1 = 1;
                g2 = 0;
                statg1 = 1;
                statg2 = 2;
            } else if (hasil[2] > 100 && hasil[2] < 127.5) {
                //rendah sedang
                g1 = (127.5 - hasil[2]) / (127.5 - 100);
                g2 = (hasil[2] - 100) / (127.5 - 100);
                statg1 = 1;
                statg2 = 2;
            } else if (hasil[2] > 127.5 && hasil[2] < 155) {
                //sedang tinggi
                g1 = (155 - hasil[2]) / (155 - 127.5);
                g2 = (hasil[2] - 127.5) / (155 - 127.5);
                statg1 = 2;
                statg2 = 3;
            } else if (hasil[2] >= 155) {
                g1 = 0;
                g2 = 1;
                statg1 = 2;
                statg2 = 3;
            } else  {
                g1 = 1;
                g2 = 1;
                statg1 = 3;
                statg2 = 3;
            }


            if (hasil[3] >= 0 && hasil[3] <= 85) {
                // rendah rendah
                b1 = 1;
                b2 = 0;
                statb1 = 1;
                statb2 = 2;
            } else if (hasil[3] > 85 && hasil[3] < 127.5) {
                // rendah sedang
                b1 = (127.5 - hasil[3]) / (127.5 - 85);
                b2 = (hasil[3] - 85) / (127.5 - 85);
                statb1 = 1;
                statb2 = 2;
            } else if (hasil[3] > 127.5 && hasil[3] < 170) {
                //sedang tinggi
                b1 = (170 - hasil[3]) / (170 - 127.5);
                b2 = (hasil[3] - 127.5) / (170 - 127.5);
                statb1 = 2;
                statb2 = 3;
            } else if (hasil[3] >= 170) {
                b1 = 0;
                b2 = 1;
                statb1 = 2;
                statb2 = 3;
            } else  {
                b1 = 1;
                b2 = 1;
                statb1 = 3;
                statb2 = 3;
            }
            //rule pertama
            if (statr1 == 1) {
                if (statg1 == 1) {
                    if (statb1 == 1) {
                        z1 = 55;
                    } else if (statb1 == 2) {
                        z1 = 55;
                    } else if (statb1 == 3) {
                        z1 = 55;
                    }
                } else if (statg1 == 2) {
                    if (statb1 == 1) {
                        z1 = 55;
                    } else if (statb1 == 2) {
                        z1 = 55;
                    } else if (statb1 == 3) {
                        z1 = 66;
                    }
                } else if (statg1 == 3) {
                    if (statb1 == 1) {
                        z1 = 55;
                    } else if (statb1 == 2) {
                        z1 = 66;
                    } else if (statb1 == 3) {
                        z1 = 77;
                    }
                }
            } else if (statr1 == 2) {
                if (statg1 == 1) {
                    if (statb1 == 1) {
                        z1 = 55;
                    } else if (statb1 == 2) {
                        z1 = 55;
                    } else if (statb1 == 3) {
                        z1 = 66;
                    }
                } else if (statg1 == 2) {
                    if (statb1 == 1) {
                        z1 = 55;
                    } else if (statb1 == 2) {
                        z1 = 66;
                    } else if (statb1 == 3) {
                        z1 = 77;
                    }
                } else if (statg1 == 3) {
                    if (statb1 == 1) {
                        z1 = 66;
                    } else if (statb1 == 2) {
                        z1 = 77;
                    } else if (statb1 == 3) {
                        z1 = 88;
                    }
                }
            } else if (statr1 == 3) {
                if (statg1 == 1) {
                    if (statb1 == 1) {
                        z1 = 55;
                    } else if (statb1 == 2) {
                        z1 = 66;
                    } else if (statb1 == 3) {
                        z1 = 77;
                    }
                } else if (statg1 == 2) {
                    if (statb1 == 1) {
                        z1 = 66;
                    } else if (statb1 == 2) {
                        z1 = 77;
                    } else if (statb1 == 3) {
                        z1 = 88;
                    }
                } else if (statg1 == 3) {
                    if (statb1 == 1) {
                        z1 = 77;
                    } else if (statb1 == 2) {
                        z1 = 88;
                    } else if (statb1 == 3) {
                        z1 = 88;
                    }
                }
            }
            //rule kedua
            if (statr2 == 1) {
                if (statg2 == 1) {
                    if (statb2 == 1) {
                        z2 = 55;
                    } else if (statb2 == 2) {
                        z2 = 55;
                    } else if (statb2 == 3) {
                        z2 = 55;
                    }
                } else if (statg2 == 2) {
                    if (statb2 == 1) {
                        z2 = 55;
                    } else if (statb2 == 2) {
                        z2 = 55;
                    } else if (statb2 == 3) {
                        z2 = 66;
                    }
                } else if (statg2 == 3) {
                    if (statb2 == 1) {
                        z2 = 55;
                    } else if (statb2 == 2) {
                        z2 = 66;
                    } else if (statb2 == 3) {
                        z2 = 77;
                    }
                }
            } else if (statr2 == 2) {
                if (statg2 == 1) {
                    if (statb2 == 1) {
                        z2 = 55;
                    } else if (statb2 == 2) {
                        z2 = 55;
                    } else if (statb2 == 3) {
                        z2 = 66;
                    }
                } else if (statg2 == 2) {
                    if (statb2 == 1) {
                        z2 = 55;
                    } else if (statb2 == 2) {
                        z2 = 66;
                    } else if (statb2 == 3) {
                        z2 = 77;
                    }
                } else if (statg2 == 3) {
                    if (statb2 == 1) {
                        z2 = 66;
                    } else if (statb2 == 2) {
                        z2 = 77;
                    } else if (statb2 == 3) {
                        z2 = 88;
                    }
                }
            } else if (statr2 == 3) {
                if (statg2 == 1) {
                    if (statb2 == 1) {
                        z2 = 55;
                    } else if (statb2 == 2) {
                        z2 = 66;
                    } else if (statb2 == 3) {
                        z2 = 77;
                    }
                } else if (statg2 == 2) {
                    if (statb2 == 1) {
                        z2 = 66;
                    } else if (statb2 == 2) {
                        z2 = 77;
                    } else if (statb2 == 3) {
                        z2 = 88;
                    }
                } else if (statg2 == 3) {
                    if (statb2 == 1) {
                        z2 = 77;
                    } else if (statb2 == 2) {
                        z2 = 88;
                    } else if (statb2 == 3) {
                        z2 = 88;
                    }
                }
            }
            apr1[0] = r1;  apr1[1] = b1; apr1[2] = g1;
            apr2[0] = r2;  apr2[1] = b2; apr2[2] = g2;
            apz1 = z1; apz2 = z2;
            jml_bar[0] = getMinValue(apr1);
            jml_bar[1] = getMinValue(apr2);
            label_bar[0] = z1;
            label_bar[1] = z2;
            double w = (getMinValue(apr1) * z1) + (getMinValue(apr2) * z2) / (getMinValue(apr1) + getMinValue(apr2));
            Log.wtf("h", "" + w);
            Log.wtf("apr1", "" + getMinValue(apr1));
            Log.wtf("apr2", "" + getMinValue(apr2));
            if (w > 0 && w <= 55) {
                hasil[5] = 2;
            } else if (w > 55 && w <= 66) {
                hasil[5] = 3;
            } else if (w > 66 && w < 77) {
                hasil[5] = 34;
            } else if (w >= 77 && w < 88) {
                hasil[5] = 4;
            } else if (w >= 88) {
                hasil[5] = 4;
            } else {
                hasil[5] = 5;
            }

            hasil[6] = getMinValueint(srgb);

            return hasil;
        }


        public int intensityLevel(final int color) {
            // also see Color#getRed(), #getBlue() and #getGreen()
            final int red = (color >> 16) & 0xFF;
            final int blue = (color >> 0) & 0xFF;
            final int green = (color >> 8) & 0xFF;
            assert red == blue && green == blue; // doesn't hold for green in RGB 565
            return MAX_INTENSITY - blue;
        }

        @Override
        protected void onPostExecute(int[] result) {
            if (result[5] == 2) {
                tv_5.setText("BWD : 2");
                tv_1.setText("5 t/ha : 75 kg/ha , 6 t/ha : 100 , 7 t/ha : 125 kg/ha , 8 t/ha : 150 kg/ha");
            }
            if (result[5] == 3) {
                tv_5.setText("BWD : 3");
                tv_1.setText("5 t/ha : 75 kg/ha , 6 t/ha : 100 kg/ha , 7 t/ha : 125 kg/ha , 8 t/ha : 150 kg/ha");
            }
            if (result[5] == 34) {
                tv_5.setText("BWD : 3");
                tv_1.setText("5 t/ha : 50 kg/ha , 6 t/ha : 75 kg/ha , 7 t/ha : 100 kg/ha , 8 t/ha : 125 kg/ha");
            }
            if (result[5] == 4) {
                tv_5.setText("BWD : 4");
                tv_1.setText("5 t/ha : 0 kg/ha , 6 t/ha : 0 kg/ha atau 50 kg/ha , 7 t/ha : 50 kg/ha , 8 t/ha : 50 kg/ha");
            }
            if (result[5] == 5) {
                tv_5.setText("BWD : 5");
                tv_1.setText("5 t/ha : 0 kg/ha , 6 t/ha : 0 kg/ha atau 50 kg/ha , 7 t/ha : 50 kg/ha , 8 t/ha : 50 kg/ha");

            }
            Log.wtf("h", "" + result[5]);
            tv_2.setText("Hasil modus S-RGB : " + result[0] + " ,modus R : " + result[1] + " modus G : " + result[2] + " modus B : " + result[3] );
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            ArrayList<String> bins = new ArrayList<>();
            int v = 0;
            for (int s : jml_bin) {
                System.out.println("jml bin : " +s);
                barEntries.add(new BarEntry((float) s,v));
                bins.add(""+v);
                v++;
            }
            BarDataSet barDataSet = new BarDataSet(barEntries, "value");
           // BarData theData1 = new BarData(bins,barDataSet);
            BarData theData = new BarData(bins, barDataSet);
            mBarChart.setData(theData);
            mBarChart.setScaleEnabled(true);
            mBarChart.setDescription("");

            XAxis xAxis = mBarChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            mBarChart.setVisibility(View.VISIBLE);

            //GRAFIK MEMBERSHIP FUNCTION R
            ArrayList<String> xAXESR = new ArrayList<>();
            ArrayList<Entry> yAXESRr = new ArrayList<>();
            ArrayList<Entry> yAXESRs = new ArrayList<>();
            ArrayList<Entry> yAXESRt = new ArrayList<>();

            float [] arrR1 = new float [256];
            float [] arrR2 = new float [256];
            float [] arrR3 = new float [256];
            for (int i = 0; i < 256; i++) {
                float r1 = 0, r2 = 0, r3= 0;
                if (i <= 82) {
                    r1 = 1;
                    r2 = 0;
                    r3 = 0;
                } else if (i > 82 && i < 127.5) {
                    r1 = (float)((127.5 - i) / (127.5 - 82));
                    r2 = (float)((i - 82) / (127.5- 82));
                    r3 = 0;
                } else if (i > 127.5 && i < 173) {
                    r1 = 0;
                    r2 = (float)((173 - i) / (173 - 127.5));
                    r3 = (float)((i - 127.5) / (173 - 127.5));
                } else if (i >= 173) {
                    r1 = 0;
                    r2 = 0;
                    r3 = 1;
                }

                arrR1 [i] = r1;
                arrR2 [i] = r2;
                arrR3 [i] = r3;
                yAXESRr.add(new Entry(r1,i));
                yAXESRs.add(new Entry(r2,i));
                yAXESRt.add(new Entry(r3,i));
                xAXESR.add(i, String.valueOf(i));
            }

            String[] xaxesR = new String[xAXESR.size()];
            for(int i=0; i<xAXESR.size();i++){
                xaxesR[i] = xAXESR.get(i).toString();
            }

            ArrayList<ILineDataSet> lineDataSetsR = new ArrayList<>();

            LineDataSet lineDataSet1 = new LineDataSet(yAXESRr,"rendah");
            lineDataSet1.setDrawCircles(false);
            lineDataSet1.setColor(Color.BLACK);

            LineDataSet lineDataSet2 = new LineDataSet(yAXESRs,"sedang");
            lineDataSet2.setDrawCircles(false);
            lineDataSet2.setColor(Color.RED);

            LineDataSet lineDataSet3 = new LineDataSet(yAXESRt,"tinggi");
            lineDataSet3.setDrawCircles(false);
            lineDataSet3.setColor(Color.BLUE);

            lineDataSetsR.add(lineDataSet1);
            lineDataSetsR.add(lineDataSet2);
            lineDataSetsR.add(lineDataSet3);

            lineChartR.setData(new LineData(xaxesR,lineDataSetsR));
            XAxis xAxisR = lineChartR.getXAxis();
            xAxisR.setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChartR.setVisibility(View.VISIBLE);

            //GRAFIK MEMBERSHIP FUNCTION G
            ArrayList<String> xAXESG = new ArrayList<>();
            ArrayList<Entry> yAXESGr = new ArrayList<>();
            ArrayList<Entry> yAXESGs = new ArrayList<>();
            ArrayList<Entry> yAXESGt = new ArrayList<>();

            float [] arrG1 = new float [256];
            float [] arrG2 = new float [256];
            float [] arrG3 = new float [256];
            for (int i = 0; i < 256; i++) {
                float g1 = 0, g2 = 0, g3= 0;
                if (i <= 100) {
                    g1 = 1;
                    g2 = 0;
                    g3 = 0;

                } else if (i > 100 && i < 127.5) {
                    g1 = (float)((127.5 - i) / (127.5 - 100));
                    g2 = (float)((i - 100) / (127.5- 100));
                    g3 = 0;
                } else if (i > 127.5 && i < 155) {
                    g1 = 0;
                    g2 = (float)((155 - i) / (155 - 127.5));
                    g3 = (float)((i - 127.5) / (155 - 127.5));
                } else if (i >= 155) {
                    g1 = 0;
                    g2 = 0;
                    g3 = 1;
                }

                arrG1 [i] = g1;
                arrG2 [i] = g2;
                arrG3 [i] = g3;

                yAXESGr.add(new Entry(g1,i));
                yAXESGs.add(new Entry(g2,i));
                yAXESGt.add(new Entry(g3,i));
                xAXESG.add(i, String.valueOf(i));
            }

            String[] xaxesG = new String[xAXESG.size()];
            for(int i=0; i<xAXESG.size();i++){
                xaxesG[i] = xAXESG.get(i).toString();
            }

            ArrayList<ILineDataSet> lineDataSetsG = new ArrayList<>();

            LineDataSet lineDataSetG1 = new LineDataSet(yAXESGr,"rendah");
            lineDataSetG1.setDrawCircles(false);
            lineDataSetG1.setColor(Color.BLACK);

            LineDataSet lineDataSetG2 = new LineDataSet(yAXESGs,"sedang");
            lineDataSetG2.setDrawCircles(false);
            lineDataSetG2.setColor(Color.RED);

            LineDataSet lineDataSetG3 = new LineDataSet(yAXESGt,"tinggi");
            lineDataSetG3.setDrawCircles(false);
            lineDataSetG3.setColor(Color.BLUE);

            lineDataSetsG.add(lineDataSetG1);
            lineDataSetsG.add(lineDataSetG2);
            lineDataSetsG.add(lineDataSetG3);

            lineChartG.setData(new LineData(xaxesG,lineDataSetsG));
            XAxis xAxisG = lineChartG.getXAxis();
            xAxisG.setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChartG.setVisibility(View.VISIBLE);

            //GRAFIK MEMBERSHIP FUNCTION B
            ArrayList<String> xAXESB = new ArrayList<>();
            ArrayList<Entry> yAXESBr = new ArrayList<>();
            ArrayList<Entry> yAXESBs = new ArrayList<>();
            ArrayList<Entry> yAXESBt = new ArrayList<>();

            float [] arrB1 = new float [256];
            float [] arrB2 = new float [256];
            float [] arrB3 = new float [256];

            for (int i = 0; i < 256; i++) {
                float b1 = 0, b2 = 0, b3= 0;
                if (i <= 85) {
                    b1 = 1;
                    b2 = 0;
                    b3 = 0;

                } else if (i > 85 && i < 127.5) {
                    b1 = (float)((127.5 - i) / (127.5 - 85));
                    b2 = (float)((i - 85) / (127.5- 85));
                    b3 = 0;
                } else if (i > 127.5 && i < 170) {
                    b1 = 0;
                    b2 = (float)((170 - i) / (170 - 127.5));
                    b3 = (float)((i - 127.5) / (170 - 127.5));
                } else if (i >= 170) {
                    b1 = 0;
                    b2 = 0;
                    b3 = 1;
                }

                arrB1 [i] = b1;
                arrB2 [i] = b2;
                arrB3 [i] = b3;
                yAXESBr.add(new Entry(b1,i));
                yAXESBs.add(new Entry(b2,i));
                yAXESBt.add(new Entry(b3,i));
                xAXESB.add(i, String.valueOf(i));
            }

            String[] xaxesB = new String[xAXESB.size()];
            for(int i=0; i<xAXESB.size();i++){
                xaxesB[i] = xAXESB.get(i).toString();
            }

            ArrayList<ILineDataSet> lineDataSetsB = new ArrayList<>();

            LineDataSet lineDataSetB1 = new LineDataSet(yAXESBr,"rendah");
            lineDataSetB1.setDrawCircles(false);
            lineDataSetB1.setColor(Color.BLACK);

            LineDataSet lineDataSetB2 = new LineDataSet(yAXESBs,"sedang");
            lineDataSetB2.setDrawCircles(false);
            lineDataSetB2.setColor(Color.RED);

            LineDataSet lineDataSetB3 = new LineDataSet(yAXESBt,"tinggi");
            lineDataSetB3.setDrawCircles(false);
            lineDataSetB3.setColor(Color.BLUE);

            lineDataSetsB.add(lineDataSetB1);
            lineDataSetsB.add(lineDataSetB2);
            lineDataSetsB.add(lineDataSetB3);

            lineChartB.setData(new LineData(xaxesB,lineDataSetsB));
            XAxis xAxisB = lineChartB.getXAxis();
            xAxisB.setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChartB.setVisibility(View.VISIBLE);

            //GRAFIK  OUTPUT
            XAxis xOAxis = barChartOutput.getXAxis();
            xOAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            YAxis yOAxisL = barChartOutput.getAxisLeft();
            yOAxisL.setAxisMaxValue(1.0f);
            yOAxisL.setAxisMinValue(0.0f);
            yOAxisL.setGranularity(0.01f);
            yOAxisL.setGranularityEnabled(true);
            YAxis yOAxisR = barChartOutput.getAxisRight();
            yOAxisR.setAxisMaxValue(1.0f);
            yOAxisR.setAxisMinValue(0.0f);
            yOAxisR.setGranularity(0.01f);
            yOAxisR.setGranularityEnabled(true);

            ArrayList<BarEntry> barOEntries = new ArrayList<>();
            ArrayList<String> barO = new ArrayList<>();

            int vO = 0;
            for (double s : jml_bar) {
                barOEntries.add(new BarEntry((float) s, label_bar[vO]));
                barO.add(""+label_bar[vO]);
                vO++;
            }

            //for (int s = 0; s < 7; s++){
            //    barOEntries.add(new BarEntry((float) ( getMinValue(apr1) +s),(apz1+s)));
            //    barO.add(""+(apz1+s));
            //    System.out.println("x = " + barOEntries.get(s).getXIndex() + " & y = " +barOEntries.get(s).getVal());
            //}

            BarDataSet barODataSet = new BarDataSet(barOEntries, "predikat");
            BarData theOData = new BarData(barO, barODataSet);
            barChartOutput.setData(theOData);
            barChartOutput.setScaleEnabled(true);
            barChartOutput.setDescription("");


            //barChartOutput.setVisibleYRangeMaximum(1, YAxis.AxisDependency.LEFT);
            barChartOutput.setVisibility(View.VISIBLE);

            mCubicValueLineChart2.addSeries(series2);
            mCubicValueLineChart2.startAnimation();

            dialog.dismiss();
            if (result[4] == 0)
                TastyToast.makeText(getApplicationContext(), "Terjadi kesalahan", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            else
                TastyToast.makeText(getApplicationContext(), "Scaning berhasil", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
        }
    }

    public static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    // getting the maximum value
    public static int getMaxValue(int j, int[] array) {
        int maxValue = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == j) {
                maxValue++;
            }
        }
        return maxValue;
    }

    // getting the miniumum value
    public static double getMinValue(double[] array) {
        double minValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
            }
        }
        return minValue;
    }

    public static int getMinValueint(int[] array) {
        int minValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
            }
        }
        return minValue;
    }
}
