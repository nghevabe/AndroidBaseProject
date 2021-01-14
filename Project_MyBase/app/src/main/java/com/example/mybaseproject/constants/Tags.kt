package com.example.mybaseproject.constants

object Tags {

    val LONG: String? = "LONG"
    val LAT: String? = "LAT"
    val MERCHANT_ID: String? = "MERCHANT_ID"
    val PHONE: String? = "PHONE"
    const val TAG: String = "SDK_Ses"

    const val SPACE = " "

    object Label {
        const val ALL_VI = "Tất cả"
        const val ALL_EN = "All"
    }

    object KILOMETER {
        const val km_50 = "<50km"
        const val km_20 = "<20km"
        const val km_10 = "<10km"
        const val km_5 = "<5km"
        const val km_2 = "<2km"
        const val km_1 = "<1km"
    }
    object DATE {
        const val ddMMyyyy = "dd/MM/yyyy"
        const val ddMMyyyy2 = "dd.MM.yyyy"
        const val EEEddMMyyy = "EEE dd.MM.yyyy"
        const val yyyymmdd = "yyyy-MM-dd"
        const val ddMM = "dd/MM"
        const val EEEE = "EEEE"
        const val EEE = "EEE"
        const val yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"
        const val HHmmssddMMyyyy = "HH:mm:ss dd.MM.yyyy"
        const val HHmmssddMMyyyy2 = "HH:mm dd.MM.yyyy"
        const val HHMM = "HH:mm"
        const val HHMMSS = "HH:mm:ss"
    }
    object SEC {
        const val BC = "se_bc_01"
        const val AE = "se_ae_02"
        const val KI = "se_ki_03"
        const val MAC = "se_mac_04"
        const val UR = "se_ur_05"
        const val US = "se_us_06"
        const val PS = "se_ps_07"
        const val P1 = "se_p1_08"
        const val P2 = "se_p2_09"
        const val P3 = "se_p3_10"
    }
    object SETTINGS {

    }
    object NUMBER {
        const val PAGE_SIZE_DEFAULT = 20
    }
    object DEFAULT_VALUE {
        const val QUERY = ""
        const val ORDER_BY = "0"
        const val TYPE = "restaurant"
        const val DISTANCE = "50000"
        const val OPENING = false
        const val PROMOTION = false
        const val LAT_HANOI = 21.0286669
        const val LON_HANOI = 105.850176
        const val CODE_CITY = "hanoi"
        const val CODE_DISTRICT = "hoankiem"
    }

    object CACHE {
        const val CODE_CITY = "codeCity"
        const val NAME_CITY = "nameCity"
        const val LAT_CITY = "latCity"
        const val LONG_CITY = "longCity"
        const val CATEGORY_TETAG = "ses_cate_tetag"
        const val CATEGORY_TETAG_DATA = "ses_cate_data"
        const val CITY_DATA = "ses_city_data"
        const val LAT_DATA = "ses_lat_data"
        const val LONG_DATA = "ses_long_data"
        const val SEARCH_KEY_WORD = "search_key_word"
        const val SUGGEST_ITEM = "suggest_item"
        const val CITY_ITEM_CURRENT = "city_item_current"
    }

    object CACHE_RECENT_ITEM {
        const val ID = "id_item_recent"
        const val NAME = "name_item_recent"
        const val IMAGE = "image_item_recent"
        const val TYPE = "type_item_recent"
        const val ADDRESS = "address_item_recent"
        const val OPEN_TIME = "open_time_recent"
        const val DISTANCE = "distance_item_recent"
        const val LIKE_NUMBER = "like_number_recent"
        const val LIKE_STATUS = "like_status_recent"

    }

    object CONSTANT_VALUE {
        const val MIN_POSX_ITEM = 720
        const val MAX_POSX_ITEM = 950
        const val HEIGHT_ITEM_RECENT = 400
        const val LOCK_POSX_ITEM = -250f
    }

    object URL_REDIRECT {
        const val URL_TEST = "https://vnticket.vnpaytest.vn/bambooapp/redirect/"
        const val URL_PRODUCT = "https://mobileapp.bambooairways.com/resultpayment/redirect/"
    }

    object PAYMENT {
        const val KEY_DATA_REQ = "pay_bav_001"
    }
}