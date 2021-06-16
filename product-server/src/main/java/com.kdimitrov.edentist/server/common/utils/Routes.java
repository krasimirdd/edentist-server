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
    public static final String BY_ID = "/{" + ID_PARAM + "}";
    public static final String ARCHIVED = "/archived";

    public static final String API = "/api";
    public static final String SERVICES = "/services";
    public static final String DOCTORS = "/doctors";
    public static final String USER = "/user";
    public static final String USER_BY_EMAIL = USER + "/{" + USEREMAIL_PARAM + "}";

    public static final String NEWSFEED = "/newsfeed";
    public static final String TOP = "/top";
    public static final String ALL = "/all";

}
