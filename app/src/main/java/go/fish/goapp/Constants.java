package go.fish.goapp;

/**
 * Created by fish on 17-2-14.
 */

public interface Constants {

//    /***TCP SIGNS***/
//    final public static String  SIGN_CMD_RESULT     = "Command Result:";
//    final public static String  SIGN_CMD_CLOSE     = "Command Close:";
//    final public static String  SIGN_CMD_SPLIT_M    = ":";
//    final public static String  SIGN_CMD_SPLIT_ARR  = "<-";
//
//
//    /***TCP CMD ACTIONS***/
//    final public static String  CMD_ACT_USER_LIST   = "user_list";

    String P_SP		    = ":::";
    String P_SP_SEND	= "->";
    String P_SP_RCV	    = "<-";
    String P_SP_ARG	    = ",";
    String P_SEND_MSG 	= "MSG";
    String P_SEND_BR	= "BROADCAST";

    String P_CALL_LOGIN		= "LOGIN";
    String P_CALL_REG		= "REGISTER";
    String P_CALL_QUIT		= "QUIT";
    String P_CALL_USER_LIST	= "USER_LIST";

    String P_RS_LOGIN		= "LOGIN";
    String P_RS_REG		    = "REGISTER";
    String P_RS_USER_LIST	= "USER_LIST";

     /**
      *  Errors
      */
    String P_RS_SUCCESS	    = "SUCCESS";
    String P_RS_ERR	        = "ERROR:";
    String E_CODE_EXISTS	= "101";		//USER IS EXISTS
    String E_CODE_PWD	    = "102";		//PWD IS WRONG


}
