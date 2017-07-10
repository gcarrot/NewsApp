package si.gcarrot.newsapp;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Urban on 7/9/17.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private LayoutInflater layoutInflater;

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.news_list_item, null);
            holder = new ViewHolder();
            holder.trialText = (TextView) convertView.findViewById(R.id.tv_trialText);
            holder.titleText = (TextView) convertView.findViewById(R.id.tv_title);
            holder.sectionText = (TextView) convertView.findViewById(R.id.tv_section);
            holder.thumbnailImg = (ImageView) convertView.findViewById(R.id.imgThumbnail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        News currentNews = getItem(position);

        if (holder.thumbnailImg != null) {
            new ImageDownloaderTask(holder.thumbnailImg).execute(currentNews.getThumbnailUrl());
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.trialText.setText(Html.fromHtml(currentNews.getTrialText(),Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.trialText.setText(Html.fromHtml(currentNews.getTrialText()));
        }

        holder.titleText.setText(currentNews.getTitle());
        holder.sectionText.setText(currentNews.getSection());

        return convertView;
    }

    public News get(int position) {
        return getItem(position);
    }


    static class ViewHolder {
        TextView trialText;
        TextView titleText;
        TextView sectionText;
        ImageView thumbnailImg;
    }

}

