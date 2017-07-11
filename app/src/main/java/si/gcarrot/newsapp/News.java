package si.gcarrot.newsapp;

/**
 * Created by Urban on 7/9/17.
 */

public class News {


    private String mTitle, mSection, mTrialText, mUrl, mThumbnail;

    public News(String Title, String Section, String trialText, String Url) {
        mTitle = Title;
        mSection = Section;
        mTrialText = trialText;
        mUrl = Url;
    }

    public News(String Title, String Section, String trialText, String Url, String thumbnailUrl) {
        mTitle = Title;
        mSection = Section;
        mTrialText = trialText;
        mThumbnail = thumbnailUrl;
        mUrl = Url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getSection() {
        return mSection;
    }

    public String getTrialText() {
        return mTrialText;
    }

    public String getThumbnailUrl() {
        return mThumbnail;
    }

    @Override
    public String toString() {
        return "News{" +
                "mTitle='" + mTitle + '\'' +
                ", mSection='" + mSection + '\'' +
                ", mTrialText='" + mTrialText + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mThumbnail='" + mThumbnail + '\'' +
                '}';
    }
}
