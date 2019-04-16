package virtuosonetsoft.pagingoffline.pagingLibrary.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import virtuosonetsoft.pagingoffline.Apiclient.ApiClient;
import virtuosonetsoft.pagingoffline.EquipmentHistory.Cons;
import virtuosonetsoft.pagingoffline.R;
import virtuosonetsoft.pagingoffline.authkey.ApplicationMain;
import virtuosonetsoft.pagingoffline.dbdatabase.HistoryFroimDb;


public class PastBookingViewHolder extends RecyclerView.ViewHolder {
    private static Context context;
    @BindView(R.id.equip_name)
    TextView equip_name;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.textView154)
    TextView status;
    @BindView(R.id.from)
    TextView todate;

    @BindView(R.id.textView44)
    TextView totaldaysorBigga;

    Date fromDate1,todate1;

    @BindView(R.id.constraintmain1)
    ConstraintLayout constraintLayout2;


    @BindView(R.id.button3)
    TextView rentagain;

//    @BindView(R.id.textView49)
//    TextView feedback;


    @BindView(R.id.textPrice)
    TextView textPrice;
    private String fromTimeWithAm,totimewithAm;

    private PastBookingViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindTo(HistoryFroimDb user) {
        equip_name.setText(user.getEquipements_types_name());
       // equip_name.setText(user.getEquipements_types_name());
        Picasso.with(context).load(ApiClient.BASE_URL + user.getEquipements_types_image()).placeholder(context.getResources().getDrawable(R.drawable.imagesmall)).error(context.getResources().getDrawable(R.drawable.imagesmall)).into(imageView);

        String pastBookingImagepath = user.getEquipements_types_image();
        ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.pastBookingImagepath, pastBookingImagepath);

        textPrice.setText("\u20B9" + user.getTotal_booking_amount());
        if (user.getStatus().equalsIgnoreCase("pending")) {
            status.setText(user.getStatus());


          //  status.setText(user.getBooking_status());
            status.setTextColor(Color.RED);
        } else if (user.getStatus().equalsIgnoreCase("complete")) {
            status.setText(user.getStatus());

            status.setTextColor(context.getResources().getColor((R.color.color_grey)));
        }
        constraintLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.booking_id1, user.getId());
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.equipement_id, user.getEquipement_id());
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue("strtotime", user.getBooking_to());
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue("strfromtime", user.getBooking_from());
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.cancelLayoutreason, "false");
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.Equipements_typesname,user.getEquipements_types_name());
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.price_type,user.getBooking_type());
                try {
                    String[] separatedfromTime = user.getBooking_from().split(" ");
                    String[] separatedToTime = user.getBooking_to().split(" ");
                    for (String item : separatedfromTime) {
                        System.out.println("item = " + item);

                        String lastOne = separatedfromTime[separatedfromTime.length - 1];
                        String lastoneToTime = separatedToTime[separatedToTime.length - 1];
                        ApplicationMain.getInstance().createLog("From Time lastOne" + lastOne);
                        final String time = lastOne;
                        final String toTime = lastoneToTime;

                        try {
                            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            final Date dateObj = sdf.parse(lastOne);
                            fromTimeWithAm = new SimpleDateFormat("hh:mm aa").format(dateObj);
                            ApplicationMain.getInstance().createLog("AmWise" + fromTimeWithAm);
                            ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.fromtimewitham, fromTimeWithAm);

                            final Date dateObj1 = sdf.parse(toTime);
                            totimewithAm = new SimpleDateFormat("hh:mm aa").format(dateObj1);
                            ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.totimewitham, totimewithAm);

                            ApplicationMain.getInstance().createLog("AmWise" + totimewithAm);
                        } catch (final ParseException e) {

                            ApplicationMain.getInstance().createLog("ParseException" + e.toString());
                        }

                    }
                }catch (Exception e)
                {

                }
               // nextActivityWithOutFinish(context, CalendarDayDecoratorActivityRentAgain.class);
            }
        });
        if (user.getBooking_type().equalsIgnoreCase("price_bighaWise")) {
            // rentText.setText(getResources().getString(R.string.rentdate1) + " " + "&" + getResources().getString(R.string.area));
            // rentDate.setText(getConvertedDateTime(ApplicationMain.getInstance().getstoreSharedpref().getSharedValue(Cons.dialogrentfromDate)) + "," +ApplicationMain.getInstance().getstoreSharedpref().getSharedValue(Cons.dialogrentoDate));
            totaldaysorBigga.setText(" "+"/"+user.getBooking_quantity()+" "+context.getResources().getString(R.string.bigha1));
        } else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                todate1 = df.parse(user.getBooking_to());
                fromDate1=df.parse(user.getBooking_from());
                ApplicationMain.getInstance().createLog("todate" + todate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            totaldaysorBigga.setText(" "+"/"+remainingday(user.getBooking_from(),user.getBooking_to())+" "+context.getResources().getString(R.string.day));
            //  rentText.setText(getResources().getString(R.string.rentdate));
            //  rentDate.setText(getConvertedDateTime(ApplicationMain.getInstance().getstoreSharedpref().getSharedValue(Cons.dialogrentfromDate)) + "," + getConvertedDateTime(ApplicationMain.getInstance().getstoreSharedpref().getSharedValue(Cons.dialogrentoDate)));
        }

        rentagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.booking_steps, "3");
                // Intent i=new Intent(context,CalendarDayDecoratorActivity.class);
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.rentagain, "true");
                ApplicationMain.getInstance().createLog("showBooking wq id" + user.getEquipement_id());
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.equipement_id, user.getEquipement_id());
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.equipement_id, user.getEquipement_id());
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.total_booking_amount, user.getTotal_booking_amount());
                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.price_type,user.getBooking_type());
                //Cons.showBookingPastHistories.add(user);
                try {
                    String[] separatedfromTime = user.getBooking_from().split(" ");
                    String[] separatedToTime = user.getBooking_to().split(" ");
                    for (String item : separatedfromTime) {
                        System.out.println("item = " + item);

                        String lastOne = separatedfromTime[separatedfromTime.length - 1];
                        String lastoneToTime = separatedToTime[separatedToTime.length - 1];
                        ApplicationMain.getInstance().createLog("From Time lastOne" + lastOne);
                        final String time = lastOne;
                        final String toTime = lastoneToTime;

                        try {
                            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            final Date dateObj = sdf.parse(lastOne);
                            fromTimeWithAm = new SimpleDateFormat("hh:mm aa").format(dateObj);
                            ApplicationMain.getInstance().createLog("AmWise" + fromTimeWithAm);
                            ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.fromtimewitham, fromTimeWithAm);

                            final Date dateObj1 = sdf.parse(toTime);
                            totimewithAm = new SimpleDateFormat("hh:mm aa").format(dateObj1);
                            ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.totimewitham, totimewithAm);

                            ApplicationMain.getInstance().createLog("AmWise" + totimewithAm);
                        } catch (final ParseException e) {

                            ApplicationMain.getInstance().createLog("ParseException" + e.toString());
                        }

                    }
                }catch (Exception e)
                {

                }
                // context.startActivity(i);
               // nextActivityWithOutFinish(context, CalendarDayDecoratorActivityRentAgain.class);
            }
        });


//        feedback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //  Intent i=new Intent(context,FeedbackActivity.class);
//                Cons.showBookingPastHistories = new ArrayList<>();
//               // Cons.showBookingPastHistories.add(user);
//                ApplicationMain.getInstance().getstoreSharedpref().storeSharedValue(Cons.booking_id,user.getId());
//                //context.startActivity(i);
//                nextActivityWithOutFinish(context, FeedbackActivity.class);
//            }
//        });
    }
    private String remainingday(String strfromtime, String strtotime) {
        String return_v = "NA";
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


        try {
            java.util.Date date1 = df1.parse(strfromtime);
            java.util.Date date2 = df1.parse(strtotime);
            long diff = date2.getTime() - date1.getTime();
            int days = (int) (diff / (1000 * 60 * 60 * 24));

            int hours = (int) ((diff - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (diff - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            if (hours < 0) {
                hours += 24;
            }
            if (min < 0) {
                float newone = (float) min / 60;
                min += 60;
                hours = (int) (hours + newone);
            }
// String c = hours+":"+min;
            String c = hours + " hours";

            Log.d("ANSWER", c);
            ApplicationMain.getInstance().createLog("date1" + date1);

            ApplicationMain.getInstance().createLog(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " Days " + c);

//System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS )); // Days : 5
            return_v = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + "";

        } catch (Exception e) {
            e.printStackTrace();
            return_v = "NA";
        }
        return return_v;

    }
    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
    private String getConvertedDateTime(String changeformat) {

        String return_v = "";
        String month = "";
        String date_ = "";
        String year_ = "";
        try {

            String[] split_v = changeformat.split(" ");
            String[] split_2 = split_v[0].split("-");
            month = Month_short_name(split_2[1]);
            date_ = split_2[2];
            year_ = split_2[0];
        } catch (Exception e) {

            month = "";
            date_ = "";
            year_ = "";
        }
        try {
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String set = dateFormat.format(df1.parse(changeformat));

            SimpleDateFormat sdf = new SimpleDateFormat("EEE");
            Date d = df1.parse(changeformat);
            String dayOfTheWeek = sdf.format(d);

            // Application.getInstance().createLog("dateeee" + set);
// Application.getInstance().createLog("final output " + dayOfTheWeek + ", " + year_ +" "+month +" "+date_);

            return_v = dayOfTheWeek + ", " + date_ + " " + month;
            //    Application.getInstance().createLog("final output " + dayOfTheWeek + ", " + date_ + " " + month);

        } catch (Exception e) {
            e.printStackTrace();
            return_v = "NA";
        }
        return return_v;
    }

    public String Month_short_name(String monthnum) {
        String returnvalue = "";

        if (monthnum.equalsIgnoreCase("1") || monthnum.equalsIgnoreCase("01"))
            returnvalue = "Jan";
        else if (monthnum.equalsIgnoreCase("2") || monthnum.equalsIgnoreCase("02"))
            returnvalue = "Feb";
        else if (monthnum.equalsIgnoreCase("3") || monthnum.equalsIgnoreCase("03"))
            returnvalue = "Mar";
        else if (monthnum.equalsIgnoreCase("4") || monthnum.equalsIgnoreCase("04"))
            returnvalue = "Apr";
        else if (monthnum.equalsIgnoreCase("5") || monthnum.equalsIgnoreCase("05"))
            returnvalue = "May";
        else if (monthnum.equalsIgnoreCase("6") || monthnum.equalsIgnoreCase("06"))
            returnvalue = "Jun";
        else if (monthnum.equalsIgnoreCase("7") || monthnum.equalsIgnoreCase("07"))
            returnvalue = "Jul";
        else if (monthnum.equalsIgnoreCase("8") || monthnum.equalsIgnoreCase("08"))
            returnvalue = "Aug";
        else if (monthnum.equalsIgnoreCase("9") || monthnum.equalsIgnoreCase("09"))
            returnvalue = "Sept";
        else if (monthnum.equalsIgnoreCase("10"))
            returnvalue = "Oct";
        else if (monthnum.equalsIgnoreCase("11"))
            returnvalue = "Nov";
        else if (monthnum.equalsIgnoreCase("12") || monthnum.equalsIgnoreCase("01"))
            returnvalue = "Dec";
        return returnvalue;
    }

    public static PastBookingViewHolder create(ViewGroup parent) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_rented_equip_history, parent, false);
        return new PastBookingViewHolder(view);
    }

    public void nextActivityWithOutFinish(Context mCon, Class cls) {

        Intent i = new Intent(mCon, cls);
        mCon.startActivity(i);
        ((Activity) mCon).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        ((Activity) mCon).finish();
    }
}

