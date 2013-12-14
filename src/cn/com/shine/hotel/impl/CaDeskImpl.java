package cn.com.shine.hotel.impl;

/*import com.mstar.tvsettingservice.CaDesk;
import com.mstar.tvsettingservice.CommonDesk;*/


import cn.com.shine.hotel.service.CaDesk;

import com.tvos.common.exception.TvCommonException;
import com.tvos.dtv.common.CaManager;
import com.tvos.dtv.common.DtvManager;
import com.tvos.dtv.common.CaManager.OnCaEventListener;

import com.tvos.dtv.vo.CACardSNInfo;
import com.tvos.dtv.vo.CARatingInfo;
import com.tvos.dtv.vo.CaACListInfo;
import com.tvos.dtv.vo.CaDetitleChkNums;
import com.tvos.dtv.vo.CaEmailContentInfo;
import com.tvos.dtv.vo.CaEmailHeadInfo;
import com.tvos.dtv.vo.CaEmailHeadsInfo;
import com.tvos.dtv.vo.CaEmailSpaceInfo;
import com.tvos.dtv.vo.CaEntitleIDs;
import com.tvos.dtv.vo.CaFeedDataInfo;
import com.tvos.dtv.vo.CaIPPVProgramInfo;
import com.tvos.dtv.vo.CaIPPVProgramInfos;
import com.tvos.dtv.vo.CaOperatorChildStatus;
import com.tvos.dtv.vo.CaOperatorIds;
import com.tvos.dtv.vo.CaOperatorInfo;
import com.tvos.dtv.vo.CaServiceEntitles;
import com.tvos.dtv.vo.CaSlotIDs;
import com.tvos.dtv.vo.CaSlotInfo;
import com.tvos.dtv.vo.CaStopIPPVBuyDlgInfo;
import com.tvos.dtv.vo.CaWorkTimeInfo;

public class CaDeskImpl extends BaseDeskImpl implements CaDesk
{
    private static CaDeskImpl caMgrImpl = null;
    private CaManager caMgr = null;
    
    private CaDeskImpl()
    {
        caMgr = CaManager.getInstance();
    }

    public static CaDeskImpl getCaMgrInstance()
    {
        if (caMgrImpl == null)
        	caMgrImpl = new CaDeskImpl();
        return caMgrImpl;
    }
    /**
     * To Get CA Card Sn Infomation
     * 
     * @return CACardSNInfo
     * @throws TvCommonException
     */
    public CACardSNInfo CaGetCardSN() throws TvCommonException
    {
    	 return caMgr.CaGetCardSN();
    }
    /**
     *  To Change CA Pin Code
     * 
     * @param1 old Pin code
     * @param2 new pin code
     * @return short
     * @throws TvCommonException
     */
    public short CaChangePin(String pbyOldPin,String pbyNewPin) throws TvCommonException
    {
    	return caMgr.CaChangePin(pbyOldPin,pbyNewPin);
    }
    /**
     * To Set CA Rating
     * 
     * @param1 Pin Code
     * @param2 New Rating
     * @return short
     * @throws TvCommonException
     */
    public short CaSetRating(String pbyPin,short byRating) throws TvCommonException
    {
    	return caMgr.CaSetRating(pbyPin,byRating);
    }
    /**
     * To Get CA Rating Information
     * 
     * @return CARatingInfo
     * @throws TvCommonException
     */
    public CARatingInfo CaGetRating() throws TvCommonException
    {
    	return caMgr.CaGetRating();
    }
    /**
     * To Modify CA Work Time
     * 
     * @param1 Pin Code
     * @param2 CA WorkTime Object
     * @return short
     * @throws TvCommonException
     */
    public short CaSetWorkTime(String pbyPin,CaWorkTimeInfo worktime) throws TvCommonException
    {
    	return caMgr.CaSetWorkTime(pbyPin,worktime);
    }
    /**
     * To Get CA WorkTime
     * 
     * @return CaWorkTimeInfo
     * @throws TvCommonException
     */
    public CaWorkTimeInfo CaGetWorkTime() throws TvCommonException
    {
    	return  caMgr.CaGetWorkTime();
    }
    /**
     * To Get CA System Version
     * 
     * @return int
     * @throws TvCommonException
     */
    public int CaGetVer() throws TvCommonException
    {
    	return  caMgr.CaGetVer();
    }
    /**
     * To Get all Ca operatorid
     * 
     * @return CaOperatorIds
     * @throws TvCommonException
     */
    public CaOperatorIds CaGetOperatorIds() throws TvCommonException
    {
    	return  caMgr.CaGetOperatorIds();
    }
    /**
     * To Get Operator information by Id
     * 
     * @param CA operator id
     * @return CaOperatorInfo
     * @throws TvCommonException
     */
    public CaOperatorInfo CaGetOperatorInfo(short wTVSID) throws TvCommonException
    {
    	return  caMgr.CaGetOperatorInfo(wTVSID);
    }
    /**
     * To Inquires the user features
     * 
     * @param CA operator id
     * @return CaACListInfo
     * @throws TvCommonException
     */
    public CaACListInfo CaGetACList(short wTVSID) throws TvCommonException
    {
    	return  caMgr.CaGetACList(wTVSID);
    }
    /**
     * To Get all CA Slot Id by Operator Id
     * 
     * @param CA operator id
     * @return CaSlotIDs
     * @throws TvCommonException
     */
    public CaSlotIDs CaGetSlotIDs(short wTVSID) throws TvCommonException
    {
    	return  caMgr.CaGetSlotIDs(wTVSID);
    }
    /**
     * To Get Slot information by OperatorId and SlotId
     * 
     * @param1 CA operator id
     * @param2 CA Slot id
     * @return CaSlotInfo
     * @throws TvCommonException
     */
    public CaSlotInfo CaGetSlotInfo(short wTVSID,short bySlotID) throws TvCommonException
    {
    	return  caMgr.CaGetSlotInfo(wTVSID, bySlotID);
    }
    /**
     * To Get Ca Service Entitle Information
     * 
     * @param CA operator id
     * @return CaServiceEntitles
     * @throws TvCommonException
     */
    public CaServiceEntitles CaGetServiceEntitles(short wTVSID) throws TvCommonException
    {
    	return  caMgr.CaGetServiceEntitles(wTVSID);
    }
    /**
     * To Get Ca Entitle Id List
     * 
     * @param CA operator id
     * @return CaEntitleIDs
     * @throws TvCommonException
     */
    public CaEntitleIDs CaGetEntitleIDs (short wTVSID) throws TvCommonException
    {
    	return  caMgr.CaGetEntitleIDs(wTVSID);
    }
    /**
     * To Get ca Detitle information
     * 
     * @param CA operator id
     * @return CaDetitleChkNums
     * @throws TvCommonException
     */
    public CaDetitleChkNums CaGetDetitleChkNums(short wTVSID) throws TvCommonException
    {
    	return  caMgr.CaGetDetitleChkNums(wTVSID);
    }
    /**
     * To Get CA Detitle information Read State
     * 
     * @param CA operator id
     * @return boolean
     * @throws TvCommonException
     */
    public boolean CaGetDetitleReaded(short wTvsID) throws TvCommonException
    {
    	return  caMgr.CaGetDetitleReaded(wTvsID);
    }
    /**
     * To Delete Detitle Check Num
     * 
     * @param1 CA operator id
     * @param2 CA Detitle Check Num
     * @return boolean
     * @throws TvCommonException
     */
    public boolean CaDelDetitleChkNum(short wTvsID, int dwDetitleChkNum ) throws TvCommonException
    {
    	return  caMgr.CaDelDetitleChkNum(wTvsID, dwDetitleChkNum);
    }
    /**
     * To Get Machine card corresponding situation
     * 
     * @param1 Smart CARDS corresponding set-top box number(MaxNum=5)
     * @param2 set-top box List
     * @return short
     * @throws TvCommonException
     */
    public short CaIsPaired(short pbyNum,String pbySTBID_List) throws TvCommonException
    {
    	return  caMgr.CaIsPaired(pbyNum, pbySTBID_List);
    }
    /**
     * To Get CA Platform Id
     * 
     * 
     * @return short
     * @throws TvCommonException
     */
    public short CaGetPlatformID() throws TvCommonException
    {
    	return  caMgr.CaGetPlatformID();
    }
	 /**
     * To Stop IPPV Buy Dialog Info
     * 
     * @param CA operator id
     * @return CaIPPVProgramInfo
     * @throws TvCommonException
     */
    public short CaStopIPPVBuyDlg(CaStopIPPVBuyDlgInfo IppvInfo) throws TvCommonException
    {
    	return caMgr.CaStopIPPVBuyDlg(IppvInfo);
    }
    /**
     * To Get Ca IPPV Program Information
     * 
     * @param CA operator id
     * @return CaIPPVProgramInfo
     * @throws TvCommonException
     */
    public CaIPPVProgramInfos CaGetIPPVProgram(short wTvsID) throws TvCommonException
    {
    	return  caMgr.CaGetIPPVProgram(wTvsID);
    }
    /**
     * To Get Ca Email Head Info  
     * 
     * 
     * @return CaEmailHeadInfo
     * @throws TvCommonException
     */
    public CaEmailHeadsInfo CaGetEmailHeads(short byCount,short byFromIndex) throws TvCommonException
    {
    	return  caMgr.CaGetEmailHeads(byCount,byFromIndex);
    }
    /**
     * To Get Ca Email Head Info  By Email Id
     * 
     * @param Email Id
     * @return CaEmailHeadInfo
     * @throws TvCommonException
     */
    public CaEmailHeadInfo CaGetEmailHead(int dwEmailID) throws TvCommonException
    {
    	return  caMgr.CaGetEmailHead(dwEmailID);
    }
    /**
     * To Get Ca Email Content By Email Id
     * 
     * @param Email Id
     * @return CaEmailContentInfo
     * @throws TvCommonException
     */
    public CaEmailContentInfo CaGetEmailContent(int dwEmailID) throws TvCommonException
    {
    	return  caMgr.CaGetEmailContent(dwEmailID);
    }
    /**
     * To Delete Email By Email Id
     * 
     * @param Email Id
     * 
     * @throws TvCommonException
     */
    public void CaDelEmail(int dwEmailID) throws TvCommonException
    {
    	caMgr.CaDelEmail(dwEmailID);
    }
    /**
     * To Inquires Email Space Info
     * 
     * 
     * @return CaEmailSpaceInfo
     * @throws TvCommonException
     */
    public CaEmailSpaceInfo CaGetEmailSpaceInfo() throws TvCommonException
    {
    	return  caMgr.CaGetEmailSpaceInfo();
    }
    /**
     * To Reads mother card and Child Card match info
     * 
     * @param CA operator id
     * @return CaOperatorChildStatus
     * @throws TvCommonException
     */
    public CaOperatorChildStatus CaGetOperatorChildStatus(short wTVSID) throws TvCommonException
    {
    	return  caMgr.CaGetOperatorChildStatus(wTVSID);
    }
    /**
     * To Read Feed Data From Parent Card
     * 
     * @param CA operator id
     * @return CaFeedDataInfo
     * @throws TvCommonException
     */
    public CaFeedDataInfo CaReadFeedDataFromParent(short wTVSID) throws TvCommonException
    {
    	return  caMgr.CaReadFeedDataFromParent(wTVSID);
    }
    /**
     * To Write Feed Data To Child Card
     * 
     * @param1 CA operator id
     * @param2 Feed Data Object
     * @return short
     * @throws TvCommonException
     */
    public short CaWriteFeedDataToChild(short wTVSID,CaFeedDataInfo FeedData) throws TvCommonException
    {
    	return  caMgr.CaWriteFeedDataToChild(wTVSID, FeedData);
    }
    /**
     * To Refresh Interface
     * 
     * @throws TvCommonException
     */
    public void CaRefreshInterface() throws TvCommonException
    {
    	caMgr.CaRefreshInterface();
    }
    /**
     * To Refresh Interface
     * 
     * @throws TvCommonException
     */
    public boolean CaOTAStateConfirm(int arg1,int arg2) throws TvCommonException
    {
    	return caMgr.CaOTAStateConfirm(arg1,arg2);
    }
    
    /**
     * Register setOnCaEventListener(OnCaEventListener listener), your listener
     * will be triggered when the events posted from native code.
     * 
     * @param listener OnCaEventListener
     */
    public void setOnCaEventListener(OnCaEventListener listener)
    {
        caMgr.setOnCaEventListener(listener);
    }
    
	 /**
     * To get current CA Event Id
     * 
     * @param
     * @return int
     */
	public int getCurrentEvent() {
		return CaManager.getCurrentEvent();
	}
	 /**
     * To get current CA message Type
     * 
     * @param 
     * @return int
     */
	public int getCurrentMsgType() {
		return CaManager.getCurrentMsgType();
	}
	 /**
     * To set current CA Event Id
     * 
     * @param CA Current Event id
     * @return 
     */
	public void setCurrentEvent(int CurrentEvent)
	{
		CaManager.setCurrentEvent(CurrentEvent);
	}
	 /**
     * To Set current CA message Type
     * 
     * @param CA Msg Type
     * @return 
     */
	public void setCurrentMsgType(int MsgType)
	{
		CaManager.setCurrentMsgType(MsgType);
	}
}
