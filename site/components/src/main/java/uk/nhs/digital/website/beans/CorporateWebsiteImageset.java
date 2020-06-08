package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:corporateWebsiteImageset")
@Node(jcrType = "website:corporateWebsiteImageset")
public class CorporateWebsiteImageset extends HippoGalleryImageSet {

    @HippoEssentialsGenerated(internalName = "website:homeBlogThumb")
    public HippoGalleryImageBean getHomeBlogThumb() {
        return getBean("website:homeBlogThumb", HippoGalleryImageBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:homeBlogThumbSmall")
    public HippoGalleryImageBean getHomeBlogThumbSmall() {
        return getBean("website:homeBlogThumbSmall", HippoGalleryImageBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:newsFeaturedPost")
    public HippoGalleryImageBean getNewsFeaturedPost() {
        return getBean("website:newsFeaturedPost", HippoGalleryImageBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:newsPostImage")
    public HippoGalleryImageBean getNewsPostImage() {
        return getBean("website:newsPostImage", HippoGalleryImageBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:newsThumbnail")
    public HippoGalleryImageBean getNewsThumbnail() {
        return getBean("website:newsThumbnail", HippoGalleryImageBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:oldArticleThumbnail")
    public HippoGalleryImageBean getOldArticleThumbnail() {
        return getBean("website:oldArticleThumbnail", HippoGalleryImageBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:pageHeaderHeroModule")
    public HippoGalleryImageBean getPageHeaderHeroModule() {
        return getBean("website:pageHeaderHeroModule", HippoGalleryImageBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:pageHeaderHeroModuleSmall")
    public HippoGalleryImageBean getPageHeaderHeroModuleSmall() {
        return getBean("website:pageHeaderHeroModuleSmall", HippoGalleryImageBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:pageHeaderSlimBanner")
    public HippoGalleryImageBean getPageHeaderSlimBanner() {
        return getBean("website:pageHeaderSlimBanner", HippoGalleryImageBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:pageHeaderSlimBannerSmall")
    public HippoGalleryImageBean getPageHeaderSlimBannerSmall() {
        return getBean("website:pageHeaderSlimBannerSmall", HippoGalleryImageBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:postImageSmall")
    public HippoGalleryImageBean getPostImageSmall() {
        return getBean("website:postImageSmall", HippoGalleryImageBean.class);
    }

}
