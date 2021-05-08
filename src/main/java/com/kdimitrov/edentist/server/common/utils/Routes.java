package com.kdimitrov.edentist.server.common.utils;

public class Routes {
    public static final String USER_PARAM = "user";
    public static final String USEREMAIL_PARAM = "userEmail";
    public static final String ID_PARAM = "id";
    public static final String CODE_HEADER = "Code";
    public static final String FILTER_HEADER = "Filter";
    public static final String AUTHORIZATION = "Authorization";

    public static final String APPOINTMENTS = "/appointments";
    public static final String SINGLE_APPOINTMENT = "/appointment";
    public static final String APPOINTMENTS_WITH_ID = APPOINTMENTS + "/{" + ID_PARAM + "}";

    private static final String API = "/api";
    public static final String API_SERVICES = API + "/services";
    public static final String API_DOCTORS = API + "/doctors";
    public static final String API_USER = API + "/user";
    public static final String API_USER_WITH_EMAIL = API_USER + "/{" + USEREMAIL_PARAM + "}";

    private static final String NEWSFEED = "/newsfeed";
    private static final String API_NEWSFEED = API + NEWSFEED;
    public static final String API_NEWSFEED_TOP = API_NEWSFEED + "/top";
    public static final String API_NEWSFEED_ALL = API_NEWSFEED + "/all";

}
