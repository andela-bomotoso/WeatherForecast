package com.example.bukola_omotoso.weatherforecast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bukola_omotoso on 24/06/16.
 */
public class WeatherArrayAdapter extends ArrayAdapter<Weather> {

private Map<String, Bitmap> bitmaps = new HashMap<>();

    public WeatherArrayAdapter(Context context, List<Weather> forecast)     {
        super(context, -1, forecast);
    }

    private static class ViewHolder {
        ImageView conditionImageView;
        TextView dayTextView;
        TextView lowTextView;
        TextView hiTextView;
        TextView humidityTextView;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)   {
        Weather day =  getItem(position);
        ViewHolder viewHolder;

        if(view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.list_item,parent, false);
            viewHolder.conditionImageView = (ImageView)view.findViewById(R.id.conditionImageView);
            viewHolder.dayTextView = (TextView) view.findViewById(R.id.dayTextView);
            viewHolder.lowTextView = (TextView)view.findViewById(R.id.lowTextView);
            viewHolder.hiTextView = (TextView)view.findViewById(R.id.hiTextView);
            viewHolder.humidityTextView = (TextView)view.findViewById(R.id.humidityTextView);
            view.setTag(viewHolder);
        }   else    {
            viewHolder = (ViewHolder)view.getTag();
        }

        if(bitmaps.containsKey(day.iconURL))    {
            viewHolder.conditionImageView.setImageBitmap(bitmaps.get(day.iconURL));
        }   else    {
            new LoadImageTask(viewHolder.conditionImageView).execute(day.iconURL);
        }
        Context context = getContext();
        viewHolder.dayTextView.setText(context.getString(R.string.day_description,day.dayOfWeek,day.description ));
        viewHolder.lowTextView.setText(context.getString(R.string.low_temp,day.minTemp));
        viewHolder.hiTextView.setText(context.getString(R.string.high_temp,day.maxTemp));
        viewHolder.humidityTextView.setText(context.getString(R.string.humidity,day.humidity));
        return view;
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        private LoadImageTask(ImageView imageView)  {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params)   {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();

                try(InputStream inputStream = connection.getInputStream())  {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.put(params[0],bitmap);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e)    {
                e.printStackTrace();
            }   finally {
                connection.disconnect();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }

    }
}
