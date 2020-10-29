package id.bnn.convey;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 39;
    public static final String DATABASE_NAME = "MITN";

    public String TABLE_SURVEYIN = "surveyin";
    public String COL_SIN_ID = "sin_id";
    public String COL_SIN_CONTAINER = "sin_container";
    public String COL_SIN_STATUS = "sin_status";
    public String COL_SIN_CREATED = "sin_created";
    public String COL_SIN_CONDITION = "sin_condition";
    public String COL_KETERANGAN = "keterangan";
    public String COL_DB_TIPE = "db_tipe";

    public String TABLE_SURVEYIN_UPLOAD = "surveyin_upload";
    public String COL_UPL_ID = "upl_id";
    public String COL_UPL_SIN_ID = "upl_sin_id";
    public String COL_UPL_DATA = "upl_data";
    public String COL_UPL_TIPE = "db_tipe";
    public String COL_UPL_TIPE_UPLOAD = "db_tipe_upload";

    public String TABLE_OWNER = "owner";
    public String COL_OW_ID = "ow_id";
    public String COL_OW_CODE = "ow_code";
    public String COL_OW_NAME = "ow_name";

    public String TABLE_WASH = "wash";
    public String COL_WAS_ID = "was_id";
    public String COL_WAS_CODE = "was_code";
    public String COL_WAS_NAME = "was_name";

    public String TABLE_SIZE = "size";
    public String COL_SIZ_ID = "siz_id";
    public String COL_SIZ_VALUE = "siz_value";

    public String TABLE_TIPE = "tipe";
    public String COL_TIP_ID = "tip_id";
    public String COL_TIP_VALUE = "tip_value";

    public String TABLE_GRADE = "grade";
    public String COL_GRD_ID = "grd_id";
    public String COL_GRD_VALUE = "grd_value";

    public String TABLE_YEAR = "year";
    public String COL_YAR_ID = "yar_id";
    public String COL_YAR_VALUE = "yar_value";

    public String TABLE_POSISI = "posisi";
    public String COL_PSI_ID = "psi_id";
    public String COL_PSI_CODE = "psi_code";
    public String COL_PSI_NAME = "psi_name";

    public String TABLE_COMPONENT = "component";
    public String COL_CMP_ID = "cmp_id";
    public String COL_CMP_CODE = "cmp_code";
    public String COL_CMP_NAME = "cmp_name";

    public String TABLE_DAMAGE = "damage";
    public String COL_DMG_ID = "dmg_id";
    public String COL_DMG_CODE = "dmg_code";
    public String COL_DMG_NAME = "dmg_name";

    public String TABLE_DIMENSION = "dimension";
    public String COL_DMN_ID = "dmn_id";
    public String COL_DMN_CODE = "dmn_code";
    public String COL_DMN_NAME = "dmn_name";

    public String TABLE_KATEGORI = "kategori";
    public String COL_KAT_ID = "kat_id";
    public String COL_KAT_NAME = "kat_name";
    public String COL_KAT_IMAGE = "kat_image";

    public String TABLE_REPAIR = "repair";
    public String COL_RPR_ID = "rpr_id";
    public String COL_RPR_CODE = "rpr_code";
    public String COL_RPR_NAME = "rpr_name";

    public String TABLE_TPC = "tpc";
    public String COL_TPC_ID = "tpc_id";
    public String COL_TPC_VALUE = "tpc_value";

    public String TABLE_UPLOAD_PHOTO = "upload_photo";
    public String COL_UPLP_ID = "uplp_id";
    public String COL_UPLP_USERID = "uplp_userid";
    public String COL_UPLP_TOKEN = "uplp_token";
    public String COL_UPLP_MOBID = "uplp_mobid";
    public String COL_UPLP_MOBID_PHOTO = "uplp_mobid_photo";
    public String COL_UPLP_MOBID_DAMAGE = "uplp_mobid_damage";
    public String COL_UPLP_LOCATION = "uplp_location";
    public String COL_UPLP_TIPE = "uplp_tipe";
    public String COL_UPLP_POSITION = "uplp_position";
    public String COL_UPLP_SURVEYID = "uplp_surveyid";
    public String COL_UPLP_STATUS = "uplp_status";
    public String COL_UPLP_COUNT = "uplp_count";
    public String COL_UPLP_TIME = "uplp_time";

    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+TABLE_SURVEYIN+" ("
                +COL_SIN_ID+" INTEGER PRIMARY KEY, "
                +COL_SIN_CONTAINER+" TEXT, "
                +COL_SIN_STATUS+" TEXT, "
                +COL_SIN_CREATED+" TEXT, "
                +COL_SIN_CONDITION+" TEXT, "
                +COL_KETERANGAN+" TEXT, "
                +COL_DB_TIPE+" TEXT "
                +")";

        String CREATE_TABLE_SURVEYIN_UPLOAD = "CREATE TABLE "+TABLE_SURVEYIN_UPLOAD+" ("
                +COL_UPL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_UPL_SIN_ID+" TEXT, "
                +COL_UPL_DATA+" TEXT, "
                +COL_UPL_TIPE+" TEXT, "
                +COL_UPL_TIPE_UPLOAD+" TEXT"
                +")";

        String CREATE_TABLE_OWNER = "CREATE TABLE "+TABLE_OWNER+" ("
                +COL_OW_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_OW_CODE+" TEXT, "
                +COL_OW_NAME+" TEXT"
                +")";

        String CREATE_TABLE_WASH = "CREATE TABLE "+TABLE_WASH+" ("
                +COL_WAS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_WAS_CODE+" TEXT, "
                +COL_WAS_NAME+" TEXT"
                +")";

        String CREATE_TABLE_KATEGORI = "CREATE TABLE "+TABLE_KATEGORI+" ("
                +COL_KAT_ID+" TEXT, "
                +COL_KAT_NAME+" TEXT,"
                +COL_KAT_IMAGE+" TEXT"
                +")";

        String CREATE_TABLE_POSISI = "CREATE TABLE "+TABLE_POSISI+" ("
                +COL_PSI_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_PSI_CODE+" TEXT, "
                +COL_PSI_NAME+" TEXT"
                +")";

        String CREATE_TABLE_COMPONENT = "CREATE TABLE "+TABLE_COMPONENT+" ("
                +COL_CMP_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_CMP_CODE+" TEXT, "
                +COL_CMP_NAME+" TEXT"
                +")";

        String CREATE_TABLE_DAMAGE = "CREATE TABLE "+TABLE_DAMAGE+" ("
                +COL_DMG_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_DMG_CODE+" TEXT, "
                +COL_DMG_NAME+" TEXT"
                +")";

        String CREATE_TABLE_REPAIR = "CREATE TABLE "+TABLE_REPAIR+" ("
                +COL_RPR_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_RPR_CODE+" TEXT, "
                +COL_RPR_NAME+" TEXT"
                +")";

        String CREATE_TABLE_DIMENSION = "CREATE TABLE "+TABLE_DIMENSION+" ("
                +COL_DMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_DMN_CODE+" TEXT, "
                +COL_DMN_NAME+" TEXT"
                +")";

        String CREATE_TABLE_SIZE = "CREATE TABLE "+TABLE_SIZE+" ("
                +COL_SIZ_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_SIZ_VALUE+" TEXT "
                +")";

        String CREATE_TABLE_TIPE = "CREATE TABLE "+TABLE_TIPE+" ("
                +COL_TIP_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_TIP_VALUE+" TEXT "
                +")";

        String CREATE_TABLE_GRADE = "CREATE TABLE "+TABLE_GRADE+" ("
                +COL_GRD_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_GRD_VALUE+" TEXT"
                +")";

        String CREATE_TABLE_YEAR = "CREATE TABLE "+TABLE_YEAR+" ("
                +COL_YAR_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_YAR_VALUE+" TEXT"
                +")";

        String CREATE_TABLE_UPLOAD_FOTO = "CREATE TABLE "+TABLE_UPLOAD_PHOTO+" ("
                +COL_UPLP_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_UPLP_USERID+" TEXT, "
                +COL_UPLP_TOKEN+" TEXT, "
                +COL_UPLP_MOBID+" TEXT, "
                +COL_UPLP_MOBID_PHOTO+" TEXT, "
                +COL_UPLP_MOBID_DAMAGE+" TEXT, "
                +COL_UPLP_LOCATION+" TEXT,"
                +COL_UPLP_POSITION+" TEXT,"
                +COL_UPLP_TIPE+" TEXT,"
                +COL_UPLP_SURVEYID+" TEXT,"
                +COL_UPLP_STATUS+" TEXT,"
                +COL_UPLP_COUNT+" TEXT,"
                +COL_UPLP_TIME+" TEXT"
                +")";

        String CREATE_TABLE_TPC = "CREATE TABLE "+TABLE_TPC+" ("
                +COL_TPC_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_TPC_VALUE+" TEXT"
                +")";

        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_SURVEYIN_UPLOAD);
        db.execSQL(CREATE_TABLE_OWNER);
        db.execSQL(CREATE_TABLE_UPLOAD_FOTO);
        db.execSQL(CREATE_TABLE_POSISI);
        db.execSQL(CREATE_TABLE_KATEGORI);
        db.execSQL(CREATE_TABLE_SIZE);
        db.execSQL(CREATE_TABLE_TIPE);
        db.execSQL(CREATE_TABLE_GRADE);
        db.execSQL(CREATE_TABLE_YEAR);
        db.execSQL(CREATE_TABLE_WASH);
        db.execSQL(CREATE_TABLE_COMPONENT);
        db.execSQL(CREATE_TABLE_DAMAGE);
        db.execSQL(CREATE_TABLE_DIMENSION);
        db.execSQL(CREATE_TABLE_REPAIR);
        db.execSQL(CREATE_TABLE_TPC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SURVEYIN);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SURVEYIN_UPLOAD);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_OWNER);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_UPLOAD_PHOTO);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_POSISI);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_KATEGORI);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SIZE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TIPE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_GRADE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_YEAR);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WASH);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_COMPONENT);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_DAMAGE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_DIMENSION);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_REPAIR);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TPC);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
