package com.kmk.iotivityclient;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import org.iotivity.base.ErrorCode;
import org.iotivity.base.ModeType;
import org.iotivity.base.ObserveType;
import org.iotivity.base.OcConnectivityType;
import org.iotivity.base.OcException;
import org.iotivity.base.OcHeaderOption;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.OcRepresentation;
import org.iotivity.base.OcResource;
import org.iotivity.base.OcResourceIdentifier;
import org.iotivity.base.PlatformConfig;
import org.iotivity.base.QualityOfService;
import org.iotivity.base.ServiceType;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final Activity mActivity = this;

//    private HashMap<OcResourceIdentifier, OcResource> mFoundResources = new HashMap<>();
//    private OcResource mFoundResource = null;
//    private int mObserverCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        test();
    }

    private void test() {
        final OcPlatform.OnResourceFoundListener onResourceFoundListener = new OcPlatform.OnResourceFoundListener() {
            @Override
            public void onResourceFound(OcResource ocResource) {
                if (ocResource == null) {
                    msg("Found resource is invalid");
                    return;
                }
                msg("Host address of the resource: " + ocResource.getHost());

                final OcResource.OnGetListener onGetListener = new OcResource.OnGetListener() {
                    @Override
                    public void onGetCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {
                        msg("GET request was successful");
                        msg("Resource URI: " + ocRepresentation.getUri());
                        try {
                            msg("" + ocRepresentation.getValue("result"));
                        } catch (OcException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onGetFailed(Throwable throwable) {
                        if (throwable instanceof OcException) {
                            OcException ocEx = (OcException) throwable;
                            ErrorCode errCode = ocEx.getErrorCode();
                            msg("Error code: " + errCode);
                        }
                        msg("Failed to GET!");
                    }
                };

                String resourceName = "/a/led";
                if (ocResource.getUri().equals(resourceName)) {
                    HashMap<String, String> queryParams = new HashMap<>();
                    try {
                        ocResource.get(queryParams, onGetListener);
                    } catch (OcException e) {
                        e.printStackTrace();
                    }
                } else {
                    msg("No resource: " + resourceName);
                }
            }

            @Override
            public void onFindResourceFailed(Throwable throwable, String s) {

            }
        };

        PlatformConfig platformConfig = new PlatformConfig(mActivity, ServiceType.IN_PROC, ModeType.CLIENT,
                "0.0.0.0", 0, QualityOfService.LOW
        );
        OcPlatform.Configure(platformConfig);
        msg("Finding all resources of type \"core.thing\".");
        String requestUri = "coap://" + "192.168.0.21" + OcPlatform.WELL_KNOWN_QUERY + "?rt=core.thing";
        try {
            OcPlatform.findResource("", requestUri, EnumSet.of(OcConnectivityType.CT_DEFAULT), onResourceFoundListener);
        } catch (OcException e) {
            e.printStackTrace();
            msg("Failed to invoke find resource API");
        }
        printLine();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

//    OcResource.OnPutListener onPutListener = new OcResource.OnPutListener() {
//        @Override
//        public void onPutCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {
//            msg("PUT request was successful");
//            msg("Resource URI: " + ocRepresentation.getUri());
//            for (OcHeaderOption o : list) {
//                msg("" + o.getOptionId() + ": " + o.getOptionData());
//            }
//            printLine();
//        }
//
//        @Override
//        public void onPutFailed(Throwable throwable) {
//            if (throwable instanceof OcException) {
//                OcException ocEx = (OcException) throwable;
//                Log.e(TAG, ocEx.toString());
//                ErrorCode errCode = ocEx.getErrorCode();
//                msg("Error code: " + errCode);
//            }
//            msg("Failed to POST!");
//        }
//    };
//    OcResource.OnDeleteListener onDeleteListener = new OcResource.OnDeleteListener() {
//        @Override
//        public void onDeleteCompleted(List<OcHeaderOption> list) {
//            msg("DELETE request was successful");
//            for (OcHeaderOption o : list) {
//                msg("" + o.getOptionId() + ": " + o.getOptionData());
//            }
//            printLine();
//        }
//
//        @Override
//        public void onDeleteFailed(Throwable throwable) {
//            if (throwable instanceof OcException) {
//                OcException ocEx = (OcException) throwable;
//                Log.e(TAG, ocEx.toString());
//                ErrorCode errCode = ocEx.getErrorCode();
//                msg("Error code: " + errCode);
//            }
//            msg("Failed to DELETE!");
//        }
//    };
//    OcResource.OnObserveListener onObserveListener = new OcResource.OnObserveListener() {
//        @Override
//        public void onObserveCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation, int i) {
//            msg("OBSERVE request was successful");
//            msg("SequenceNumber:" + i);
//            msg("Resource URI: " + ocRepresentation.getUri());
//            try {
//                msg("" + ocRepresentation.getValue("result"));
//            } catch (OcException e) {
//                e.printStackTrace();
//            }
//            if (mObserverCount++ == 10) {
//                try {
//                    mFoundResource.cancelObserve();
//                    mObserverCount = 0;
//                } catch (OcException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        @Override
//        public void onObserveFailed(Throwable throwable) {
//            if (throwable instanceof OcException) {
//                OcException ocEx = (OcException) throwable;
//                Log.e(TAG, ocEx.toString());
//                ErrorCode errCode = ocEx.getErrorCode();
//                msg("Error code: " + errCode);
//            }
//            msg("Failed to OBSERVE!");
//        }
//    };
//
//    private void findResource() {
//        OcPlatform.OnResourceFoundListener onResourceFoundListener = new OcPlatform.OnResourceFoundListener() {
//            @Override
//            public void onResourceFound(OcResource ocResource) {
//                if (ocResource == null) {
//                    msg("Found resource is invalid");
//                    return;
//                }
//
//                if (mFoundResources.containsKey(ocResource.getUniqueIdentifier())) {
//                    msg("Found a previously seen resource again!");
//                } else {
//                    msg("Found resource for the first time on server with ID: " + ocResource.getServerId());
//                    mFoundResources.put(ocResource.getUniqueIdentifier(), ocResource);
//                }
//
//                msg("URI of the resource: " + ocResource.getUri());
//                msg("Host address of the resource: " + ocResource.getHost());
//                msg("List of resource types: ");
//                for (String resourceType : ocResource.getResourceTypes()) {
//                    msg("" + resourceType);
//                }
//                msg("List of resource interfaces:");
//                for (String resourceInterface : ocResource.getResourceInterfaces()) {
//                    msg("" + resourceInterface);
//                }
//                msg("List of resource connectivity types:");
//                for (OcConnectivityType connectivityType : ocResource.getConnectivityTypeSet()) {
//                    msg("" + connectivityType);
//                }
//                printLine();
////                if (ocResource.getUri().equals("/a/temp")) {
//                mFoundResource = ocResource;
////                } else {
////                    disableRequest();
////                }
//            }
//
//            @Override
//            public void onFindResourceFailed(Throwable throwable, String s) {
//                msg(s);
//            }
//        };
//
//        PlatformConfig platformConfig = new PlatformConfig(mActivity, ServiceType.IN_PROC, ModeType.CLIENT,
//                "0,0,0,0", 0, QualityOfService.LOW
//        );
//        OcPlatform.Configure(platformConfig);
//        msg("Finding all resources of type \"core.thing" + "\".");
//        String requestUri = "coap://" + "1.1.1.1" + OcPlatform.WELL_KNOWN_QUERY + "?rt=core.thing";
//        Log.i(TAG, requestUri);
//        try {
//            OcPlatform.findResource("", requestUri, EnumSet.of(OcConnectivityType.CT_DEFAULT), onResourceFoundListener);
//        } catch (OcException e) {
//            e.printStackTrace();
//            msg("Failed to invoke find resource API");
//        }
//        printLine();
//    }

//    private void getResourceRepresentation() {
//        OcResource.OnGetListener onGetListener = new OcResource.OnGetListener() {
//            @Override
//            public void onGetCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {
//                msg("GET request was successful");
//                msg("Resource URI: " + ocRepresentation.getUri());
//                try {
//                    msg("" + ocRepresentation.getValue("result"));
//                } catch (OcException e) {
//                    e.printStackTrace();
//                }
//                printLine();
//            }
//
//            @Override
//            public void onGetFailed(Throwable throwable) {
//                if (throwable instanceof OcException) {
//                    OcException ocEx = (OcException) throwable;
//                    ErrorCode errCode = ocEx.getErrorCode();
//                    msg("Error code: " + errCode);
//                }
//                msg("Failed to GET!");
//            }
//        };
//
//        msg("Getting Representation...");
//        HashMap<String, String> queryParams = new HashMap<>();
//        try {
//            mFoundResource.get(queryParams, onGetListener);
//        } catch (OcException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void postResourceRepresentation() {
//        OcResource.OnPostListener onPostListener = new OcResource.OnPostListener() {
//            @Override
//            public void onPostCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {
//                msg("POST request was successful");
//                msg("Resource URI: " + ocRepresentation.getUri());
//                try {
//                    msg("" + ocRepresentation.getValue("result"));
//                } catch (OcException e) {
//                    e.printStackTrace();
//                }
//                printLine();
//            }
//
//            @Override
//            public void onPostFailed(Throwable throwable) {
//                if (throwable instanceof OcException) {
//                    OcException ocEx = (OcException) throwable;
//                    Log.e(TAG, ocEx.toString());
//                    ErrorCode errCode = ocEx.getErrorCode();
//                    msg("Error code: " + errCode);
//                }
//                msg("Failed to POST!");
//            }
//        };
//
//        msg("Posting Representation...");
//        HashMap<String, String> queryParams = new HashMap<>();
//        OcRepresentation ocRepresentation = new OcRepresentation();
//        try {
//            ocRepresentation.setValue("what", "Hello");
//            mFoundResource.post(ocRepresentation, queryParams, onPostListener);
//        } catch (OcException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void putResourceRepresentation() {
//        msg("Putting Representation...");
//        HashMap<String, String> queryParams = new HashMap<>();
//        OcRepresentation ocRepresentation = new OcRepresentation();
//        try {
//            ocRepresentation.setValue("what", "Hello World!");
//            mFoundResource.put(ocRepresentation, queryParams, onPutListener);
//        } catch (OcException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void deleteResourceRepresentation() {
//        msg("Deleting Representation...");
//        try {
//            mFoundResource.deleteResource(onDeleteListener);
//        } catch (OcException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void observeResourceRepresentation() {
//        msg("Putting Representation...");
//        HashMap<String, String> queryParams = new HashMap<>();
//        try {
//            mFoundResource.observe(ObserveType.OBSERVE, queryParams, onObserveListener);
//        } catch (OcException e) {
//            e.printStackTrace();
//        }
//    }

    private void msg(final String text) {
        Log.i(TAG, text);
    }

    private void printLine() {
        msg("---------------------------------");
    }
}
