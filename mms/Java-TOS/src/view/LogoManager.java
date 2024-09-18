package view;

import java.io.InputStream;
import javafx.scene.image.Image;

public class LogoManager {
    private static LogoManager instance;

    public static LogoManager getInstance(){
        if(instance == null){
            instance = new LogoManager();
        }
        return instance;
    }

    public Image getSplash() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_splash_image.png");
        return new Image(icon_stream);
    }

    public Image getLogo() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_logo.png");
        return new Image(icon_stream);
    }

    public Image getCloseIcon() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_close.png");
        return new Image(icon_stream);
    }

    public Image getBannerLogo() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_top_logo.png");
        return new Image(icon_stream);
    }

    public Image getMenuIcon() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_menu.png");
        return new Image(icon_stream);
    }
    public Image getMenuCloseIcon() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_arrow_back.png");
        return new Image(icon_stream);
    }
    public Image getAlertIcon() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_alert.png");
        return new Image(icon_stream);
    }
    public Image getKeylockIcon() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_keylock.png");
        return new Image(icon_stream);
    }
    public Image getArrowDownIcon() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_arrow_down.png");
        return new Image(icon_stream);
    }

    public Image getLoginIcon() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_login.png");
        return new Image(icon_stream);
    }

    public Image getExitIcon() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_exit.png");
        return new Image(icon_stream);
    }

    public Image getLicenseIcon() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_licensekey.png");
        return new Image(icon_stream);
    }

    public Image getTooltipImage() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_tooltip.png");
        return new Image(icon_stream);
    }

    public Image getMenuDashImage() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_menu_dashboard.png");
        return new Image(icon_stream);
    }

    public Image getMenuSettingImage() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_menu_setting.png");
        return new Image(icon_stream);
    }

    public Image getMenuInstallImage() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_menu_install.png");
        return new Image(icon_stream);
    }

    public Image getMenuGraphImage() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_menu_graph.png");
        return new Image(icon_stream);
    }

    public Image getMenuTableView() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_menu_tableview.png");
        return new Image(icon_stream);
    }

    public Image getMenuTimeSavingImage() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_menu_timesavings.png");
        return new Image(icon_stream);
    }

    public Image getMenuMaintenanceImage() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_menu_maintenance.png");
        return new Image(icon_stream);
    }

    public Image getMenuMMSImage() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_menu_mms.png");
        return new Image(icon_stream);
    }

    public Image getMenuCheckUpdateImage() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_menu_update.png");
        return new Image(icon_stream);
    }

    public Image getStatusOnIcon() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_status_on.png");
        return new Image(icon_stream);
    }

    public Image getStatusOffIcon() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_status_off.png");
        return new Image(icon_stream);
    }

    public Image getStatusSemiIcon() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_status_semi.png");
        return new Image(icon_stream);
    }

    public Image getFolderIcon() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_folder.png");
        return new Image(icon_stream);
    }

    public String getURL(String location) {
        String path = getClass().getClassLoader().getResource(location).toExternalForm();
        return path;
    }

    public InputStream getLogconfig(){
        return getClass().getClassLoader().getResourceAsStream("resource/style/mylogging.properties");
    }

    public Image getAvatarLogo() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_guest.png");
        return new Image(icon_stream);
    }

    public Image getImgGoodUp() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_goodsup.png");
        return new Image(icon_stream);
    }

    public Image getImgGoodDown() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_goodsdown.png");
        return new Image(icon_stream);
    }

    public Image getImgBadUp() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_badsup.png");
        return new Image(icon_stream);
    }

    public Image getImgBadDown() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_badsdown.png");
        return new Image(icon_stream);
    }

    public Image getImgJobEdit() {
        InputStream icon_stream = getClass().getClassLoader().getResourceAsStream("resource/images/ic_jobid_edit_png.png");
        return new Image(icon_stream);
    }
}
