package cn.com.shine.hotel.service;

import com.tvos.common.exception.TvCommonException;
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
import com.tvos.dtv.vo.CaIPPVProgramInfos;
import com.tvos.dtv.vo.CaOperatorChildStatus;
import com.tvos.dtv.vo.CaOperatorIds;
import com.tvos.dtv.vo.CaOperatorInfo;
import com.tvos.dtv.vo.CaServiceEntitles;
import com.tvos.dtv.vo.CaSlotIDs;
import com.tvos.dtv.vo.CaSlotInfo;
import com.tvos.dtv.vo.CaStopIPPVBuyDlgInfo;
import com.tvos.dtv.vo.CaWorkTimeInfo;

public  interface CaDesk extends BaseDesk
{
    /**
     * To Get CA Card Sn Infomation
     * 
     * @return CACardSNInfo
     * @throws TvCommonException
     */
	public CACardSNInfo CaGetCardSN() throws TvCommonException;
    /**
     *  To Change CA Pin Code
     * 
     * @param1 old Pin code
     * @param2 new pin code
     * @return short
     * @throws TvCommonException
     */
    public short CaChangePin(String pbyOldPin,String pbyNewPin) throws TvCommonException;
    /**
     * To Set CA Rating
     * 
     * @param1 Pin Code
     * @param2 New Rating
     * @return short
     * @throws TvCommonException
     */
    public short CaSetRating(String pbyPin,short byRating) throws TvCommonException;
    /**
     * To Get CA Rating Information
     * 
     * @return CARatingInfo
     * @throws TvCommonException
     */
    public CARatingInfo CaGetRating() throws TvCommonException;
    /**
     * To Modify CA Work Time
     * 
     * @param1 Pin Code
     * @param2 CA WorkTime Object
     * @return short
     * @throws TvCommonException
     */
    public short CaSetWorkTime(String pbyPin,CaWorkTimeInfo worktime) throws TvCommonException;
    /**
     * To Get CA WorkTime
     * 
     * @return CaWorkTimeInfo
     * @throws TvCommonException
     */
    public CaWorkTimeInfo CaGetWorkTime() throws TvCommonException;
    /**
     * To Get CA System Version
     * 
     * @return int
     * @throws TvCommonException
     */
    public int CaGetVer() throws TvCommonException;
    /**
     * To Get all Ca operatorid
     * 
     * @return CaOperatorIds
     * @throws TvCommonException
     */
    public CaOperatorIds CaGetOperatorIds() throws TvCommonException;
    /**
     * To Get Operator information by Id
     * 
     * @param CA operator id
     * @return CaOperatorInfo
     * @throws TvCommonException
     */
   public CaOperatorInfo CaGetOperatorInfo(short wTVSID) throws TvCommonException;
    /**
     * To Inquires the user features
     * 
     * @param CA operator id
     * @return CaACListInfo
     * @throws TvCommonException
     */
    public CaACListInfo CaGetACList(short wTVSID) throws TvCommonException;
    /**
     * To Get all CA Slot Id by Operator Id
     * 
     * @param CA operator id
     * @return CaSlotIDs
     * @throws TvCommonException
     */
   public CaSlotIDs CaGetSlotIDs(short wTVSID) throws TvCommonException;
    /**
     * To Get Slot information by OperatorId and SlotId
     * 
     * @param1 CA operator id
     * @param2 CA Slot id
     * @return CaSlotInfo
     * @throws TvCommonException
     */
    public CaSlotInfo CaGetSlotInfo(short wTVSID,short bySlotID) throws TvCommonException;
    /**
     * To Get Ca Service Entitle Information
     * 
     * @param CA operator id
     * @return CaServiceEntitles
     * @throws TvCommonException
     */
   public CaServiceEntitles CaGetServiceEntitles(short wTVSID) throws TvCommonException;
    /**
     * To Get Ca Entitle Id List
     * 
     * @param CA operator id
     * @return CaEntitleIDs
     * @throws TvCommonException
     */
   public CaEntitleIDs CaGetEntitleIDs (short wTVSID) throws TvCommonException;
    /**
     * To Get ca Detitle information
     * 
     * @param CA operator id
     * @return CaDetitleChkNums
     * @throws TvCommonException
     */
   public CaDetitleChkNums CaGetDetitleChkNums(short wTVSID) throws TvCommonException;
    /**
     * To Get CA Detitle information Read State
     * 
     * @param CA operator id
     * @return boolean
     * @throws TvCommonException
     */
    public boolean CaGetDetitleReaded(short wTvsID) throws TvCommonException;
    /**
     * To Delete Detitle Check Num
     * 
     * @param1 CA operator id
     * @param2 CA Detitle Check Num
     * @return boolean
     * @throws TvCommonException
     */
    public boolean CaDelDetitleChkNum(short wTvsID, int dwDetitleChkNum ) throws TvCommonException;
    /**
     * To Get Machine card corresponding situation
     * 
     * @param1 Smart CARDS corresponding set-top box number(MaxNum=5)
     * @param2 set-top box List
     * @return short
     * @throws TvCommonException
     */
    public short CaIsPaired(short pbyNum,String pbySTBID_List) throws TvCommonException;
    /**
     * To Get CA Platform Id
     * 
     * 
     * @return short
     * @throws TvCommonException
     */
    public short CaGetPlatformID() throws TvCommonException;
	 /**
     * To Stop IPPV Buy Dialog Info
     * 
     * @param CA operator id
     * @return CaIPPVProgramInfo
     * @throws TvCommonException
     */
   public short CaStopIPPVBuyDlg(CaStopIPPVBuyDlgInfo IppvInfo) throws TvCommonException;
    /**
     * To Get Ca IPPV Program Information
     * 
     * @param CA operator id
     * @return CaIPPVProgramInfo
     * @throws TvCommonException
     */
   public CaIPPVProgramInfos CaGetIPPVProgram(short wTvsID) throws TvCommonException;
    /**
     * To Get Ca Email Head Info  
     * 
     * 
     * @return CaEmailHeadInfo
     * @throws TvCommonException
     */
    public CaEmailHeadsInfo CaGetEmailHeads(short byCount,short byFromIndex) throws TvCommonException;
    /**
     * To Get Ca Email Head Info  By Email Id
     * 
     * @param Email Id
     * @return CaEmailHeadInfo
     * @throws TvCommonException
     */
   public CaEmailHeadInfo CaGetEmailHead(int dwEmailID) throws TvCommonException;
    /**
     * To Get Ca Email Content By Email Id
     * 
     * @param Email Id
     * @return CaEmailContentInfo
     * @throws TvCommonException
     */
    public CaEmailContentInfo CaGetEmailContent(int dwEmailID) throws TvCommonException;
  /**
     * To Delete Email By Email Id
     * 
     * @param Email Id
     * 
     * @throws TvCommonException
     */
    public void CaDelEmail(int dwEmailID) throws TvCommonException;
    /**
     * To Inquires Email Space Info
     * 
     * 
     * @return CaEmailSpaceInfo
     * @throws TvCommonException
     */
   public CaEmailSpaceInfo CaGetEmailSpaceInfo() throws TvCommonException;
    /**
     * To Reads mother card and Child Card match info
     * 
     * @param CA operator id
     * @return CaOperatorChildStatus
     * @throws TvCommonException
     */
   public CaOperatorChildStatus CaGetOperatorChildStatus(short wTVSID) throws TvCommonException;
    /**
     * To Read Feed Data From Parent Card
     * 
     * @param CA operator id
     * @return CaFeedDataInfo
     * @throws TvCommonException
     */
    public CaFeedDataInfo CaReadFeedDataFromParent(short wTVSID) throws TvCommonException;
    /**
     * To Write Feed Data To Child Card
     * 
     * @param1 CA operator id
     * @param2 Feed Data Object
     * @return short
     * @throws TvCommonException
     */
   public short CaWriteFeedDataToChild(short wTVSID,CaFeedDataInfo FeedData) throws TvCommonException;
    /**
     * To Refresh Interface
     * 
     * @throws TvCommonException
     */
    public void CaRefreshInterface() throws TvCommonException;
    /**
     * To CaOTAStateConfirm Interface
     * 
     * @throws TvCommonException
     */
    public boolean CaOTAStateConfirm(int arg1,int arg2) throws TvCommonException;
    /**
     * Register setOnCaEventListener(OnCaEventListener listener), your listener
     * will be triggered when the events posted from native code.
     * 
     * @param listener OnCaEventListener
     */
    public void setOnCaEventListener(OnCaEventListener listener);
    
	 /**
     * To get current CA Event Id
     * 
     * @param
     * @return int
     */
	public int getCurrentEvent();
	/**
     * To get current CA message Type
     * 
     * @param 
     * @return int
     */
	public int getCurrentMsgType();
	 /**
     * To set current CA Event Id
     * 
     * @param CA Current Event id
     * @return 
     */
	public void setCurrentEvent(int CurrentEvent);
	 /**
     * To Set current CA message Type
     * 
     * @param CA Msg Type
     * @return 
     */
	public void setCurrentMsgType(int MsgType);
    
    
}
