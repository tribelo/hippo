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
}
