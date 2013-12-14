/**
 * MStar Software
 * Copyright (c) 2011 - 2012 MStar Semiconductor, Inc. All rights reserved.
 *
 * All software, firmware and related documentation herein (“MStar Software�? are
 * intellectual property of MStar Semiconductor, Inc. (“MStar�? and protected by
 * law, including, but not limited to, copyright law and international treaties.
 * Any use, modification, reproduction, retransmission, or republication of all
 * or part of MStar Software is expressly prohibited, unless prior written
 * permission has been granted by MStar.
 *
 * By accessing, browsing and/or using MStar Software, you acknowledge that you
 * have read, understood, and agree, to be bound by below terms (“Terms�? and to
 * comply with all applicable laws and regulations:
 *
 * 1. MStar shall retain any and all right, ownership and interest to MStar
 * Software and any modification/derivatives thereof.  No right, ownership,
 * or interest to MStar Software and any modification/derivatives thereof is
 * transferred to you under Terms.
 *
 * 2. You understand that MStar Software might include, incorporate or be supplied
 * together with third party’s software and the use of MStar Software may require
 * additional licenses from third parties.  Therefore, you hereby agree it is your
 * sole responsibility to separately obtain any and all third party right and
 * license necessary for your use of such third party’s software.
 *
 * 3. MStar Software and any modification/derivatives thereof shall be deemed as
 * MStar’s confidential information and you agree to keep MStar’s confidential
 * information in strictest confidence and not disclose to any third party.
 *
 * 4. MStar Software is provided on an “AS IS�?basis without warranties of any kind.
 * Any warranties are hereby expressly disclaimed by MStar, including without
 * limitation, any warranties of merchantability, non-infringement of intellectual
 * property rights, fitness for a particular purpose, error free and in conformity
 * with any international standard.  You agree to waive any claim against MStar for
 * any loss, damage, cost or expense that you may incur related to your use of MStar
 * Software.  In no event shall MStar be liable for any direct, indirect, incidental
 * or consequential damages, including without limitation, lost of profit or revenues,
 * lost or damage of data, and unauthorized system use.  You agree that this Section 4
 * shall still apply without being affected even if MStar Software has been modified
 * by MStar in accordance with your request or instruction for your use, except
 * otherwise agreed by both parties in writing.
 *
 * 5. If requested, MStar may from time to time provide technical supports or
 * services in relation with MStar Software to you for your use of MStar Software
 * in conjunction with your or your customer’s product (“Services�?.  You understand
 * and agree that, except otherwise agreed by both parties in writing, Services are
 * provided on an “AS IS�?basis and the warranty disclaimer set forth in Section 4
 * above shall apply.
 *
 * 6. Nothing contained herein shall be construed as by implication, estoppels or
 * otherwise: (a) conferring any license or right to use MStar name, trademark,
 * service mark, symbol or any other identification; (b) obligating MStar or any
 * of its affiliates to furnish any person, including without limitation, you and
 * your customers, any assistance of any kind whatsoever, or any information; or
 * (c) conferring any license or right under any intellectual property right.
 *
 * 7. These terms shall be governed by and construed in accordance with the laws
 * of Taiwan, R.O.C., excluding its conflict of law rules.  Any and all dispute
 * arising out hereof or related hereto shall be finally settled by arbitration
 * referred to the Chinese Arbitration Association, Taipei in accordance with
 * the ROC Arbitration Law and the Arbitration Rules of the Association by three (3)
 * arbitrators appointed in accordance with the said Rules.  The place of
 * arbitration shall be in Taipei, Taiwan and the language shall be English.
 * The arbitration award shall be final and binding to both parties.
 */
package cn.com.shine.hotel.impl;

import java.util.ArrayList;


import cn.com.shine.hotel.service.CommonDesk;
import cn.com.shine.hotel.service.EpgDesk;

/*import com.mstar.tvsettingservice.CommonDesk;
import com.mstar.tvsettingservice.EpgDesk;*/
import com.tvos.common.TimerManager;
import com.tvos.common.TvManager;
import com.tvos.common.exception.TvCommonException;
import com.tvos.common.vo.EpgEventTimerInfo;
import com.tvos.common.vo.PresentFollowingEventInfo;
import com.tvos.common.vo.TvOsType;
import com.tvos.dtv.common.DtvManager;
import com.tvos.dtv.common.EpgManager;
import com.tvos.dtv.vo.DtvEitInfo;
import com.tvos.dtv.vo.DtvType.EnumEpgDescriptionType;
import com.tvos.dtv.vo.DtvEventComponentInfo;
import com.tvos.dtv.vo.EpgCridEventInfo;
import com.tvos.dtv.vo.EpgCridStatus;
import com.tvos.dtv.vo.EpgEventInfo;
import com.tvos.dtv.vo.EpgFirstMatchHdCast;
import com.tvos.dtv.vo.EpgHdSimulcast;
import com.tvos.dtv.vo.EpgTrailerLinkInfo;
import com.tvos.dtv.vo.NvodEventInfo;

import android.text.format.Time;
import android.util.Log;


public class EpgDeskImpl extends BaseDeskImpl implements EpgDesk
{
	private static EpgDeskImpl epgMgrImpl = null;
	private CommonDesk com = null;
	private EpgManager epgMgr = null;
	private TimerManager tm = TvManager.getTimerManager();

	private EpgDeskImpl()
	{
		com = CommonDeskImpl.getInstance();
		com.printfI("TvService", "EpgManagerImpl constructor!!");
		try
		{
			epgMgr = DtvManager.getEpgManager();
		}
		catch (TvCommonException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static EpgDeskImpl getEpgMgrInstance()
	{
		if (epgMgrImpl == null)
			epgMgrImpl = new EpgDeskImpl();
		return epgMgrImpl;
	}

	/**
	 * Get total event info from u32BaseTime
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNo
	 *            int Program service number.
	 * @param baseTime
	 *            Time base time based on UTC time
	 * @param maxEventInfoCount
	 *            int maximum object size in return arraylist
	 * @return ArrayList<EpgEventInfo>
	 * @throws TvCommonException
	 */
	public ArrayList<EpgEventInfo> getEventInfo(short serviceType, int serviceNo, Time baseTime, int maxEventInfoCount)
	        throws TvCommonException
	{
		return epgMgr.getEventInfo(serviceType, serviceNo, baseTime, maxEventInfoCount);
	}

	/**
	 * Get total event counts from base time
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNo
	 *            int Program service number.
	 * @param baseTime
	 *            Time UTC time
	 * @return int event count from basetime
	 */
	public int getEventCount(short serviceType, int serviceNo, Time baseTime)
	{
		try
		{
			return epgMgr.getEventCount(serviceType, serviceNo, baseTime);
		}
		catch (TvCommonException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Get program EPG event information by time.
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNo
	 *            int Program service number.
	 * @param baseTime
	 *            Time base time based on UTC time
	 * @return EpgEventInfo
	 * @throws TvCommonException
	 */
	public EpgEventInfo getEventInfoByTime(short serviceType, int serviceNo, Time baseTime) throws TvCommonException
	{
		return epgMgr.getEventInfoByTime(serviceType, serviceNo, baseTime);
	}

	/**
	 * Get program EPG event information by event ID.
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNo
	 *            int Program service number.
	 * @param eventID
	 *            short
	 * @return EpgEventInfo
	 * @throws TvCommonException
	 */
	public EpgEventInfo getEventInfoById(short serviceType, int serviceNo, short eventID) throws TvCommonException
	{
		return epgMgr.getEventInfoById(serviceType, serviceNo, eventID);
	}

	/**
	 * Get program EPG event descriptor.
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNumber
	 *            int Program service number.
	 * @param baseTime
	 *            Time UTC time
	 * @param epgDescriptionType
	 *            EN_EPG_DESCRIPTION_TYPE
	 * @return String
	 * @throws TvCommonException
	 */
	public String getEventDescriptor(short serviceType, int serviceNumber, Time baseTime,
	        EnumEpgDescriptionType epgDescriptionType) throws TvCommonException
	{
		return epgMgr.getEventDescriptor(serviceType, serviceNumber, baseTime, epgDescriptionType);
	}

	/**
	 * Get EPG HD simulcast
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNo
	 *            int Program service number.
	 * @param baseTime
	 *            Time UTC time
	 * @param maxCount
	 *            short maximum count want to get
	 * @return EpgHdSimulcast
	 * @throws TvCommonException
	 */
	public ArrayList<EpgHdSimulcast> getEventHdSimulcast(short serviceType, int serviceNo, Time baseTime, short maxCount)
	        throws TvCommonException
	{
		return epgMgr.getEventHdSimulcast(serviceType, serviceNo, baseTime, maxCount);
	}

	/**
	 * Reset the service priority to default for the case EPG database is full.
	 *
	 * @throws TvCommonException
	 */
	public void resetEpgProgramPriority() throws TvCommonException
	{
		epgMgr.resetEpgProgramPriority();
	}

	/**
	 * Get CRID status. Series / split / alternate / recommend.
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNumber
	 *            int Program service number.
	 * @param eventStartTime
	 *            Time Target event start time base on UTC time.
	 * @return EpgCridStatus EPG CRID Status
	 * @throws TvCommonException
	 */
	public EpgCridStatus getCridStatus(short serviceType, int serviceNumber, Time eventStartTime)
	        throws TvCommonException
	{
		return epgMgr.getCridStatus(serviceType, serviceNumber, eventStartTime);
	}

	/**
	 * Get CRID series list.
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNumber
	 *            int Program service number.
	 * @param eventStartTime
	 *            Time Target event start time base on UTC time.
	 * @return ArrayList<EpgCridEventInfo> ArrayList of EpgCridEventInfo
	 * @throws TvCommonException
	 */
	public ArrayList<EpgCridEventInfo> getCridSeriesList(short serviceType, int serviceNumber, Time eventStartTime)
	        throws TvCommonException
	{
		return epgMgr.getCridSeriesList(serviceType, serviceNumber, eventStartTime);
	}

	/**
	 * Get CRID split list.
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNumber
	 *            int Program service number.
	 * @param eventStartTime
	 *            Time Target event start time base on UTC time.
	 * @return ArrayList<EpgCridEventInfo> ArrayList of EpgCridEventInfo
	 * @throws TvCommonException
	 */
	public ArrayList<EpgCridEventInfo> getCridSplitList(short serviceType, int serviceNumber, Time eventStartTime)
	        throws TvCommonException
	{
		return epgMgr.getCridSplitList(serviceType, serviceNumber, eventStartTime);
	}

	/**
	 * Get CRID alternate list.
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNumber
	 *            int Program service number.
	 * @param eventStartTime
	 *            Time event start time based on UTC time
	 * @return ArrayList<EpgCridEventInfo> vector of ST_DTV_EPG_CRID_EVENT_INFO
	 * @throws TvCommonException
	 */
	public ArrayList<EpgCridEventInfo> getCridAlternateList(short serviceType, int serviceNumber, Time eventStartTime)
	        throws TvCommonException
	{
		return epgMgr.getCridAlternateList(serviceType, serviceNumber, eventStartTime);
	}

	/**
	 * Get trailer link
	 *
	 * @return ArrayList<EpgTrailerLinkInfo> arraylist of EPG trailer link
	 *         infomation
	 * @throws TvCommonException
	 */
	public ArrayList<EpgTrailerLinkInfo> getRctTrailerLink() throws TvCommonException
	{
		return epgMgr.getRctTrailerLink();
	}

	/**
	 * Get event info(s) by trailer link
	 *
	 * @param epgTrailerLinkInfo
	 *            EpgTrailerLinkInfo
	 * @return ArrayList<EpgCridEventInfo> arraylist of EpgCridEventInfo
	 * @throws TvCommonException
	 */
	public ArrayList<EpgCridEventInfo> getEventInfoByRctLink(EpgTrailerLinkInfo epgTrailerLinkInfo)
	        throws TvCommonException
	{
		return epgMgr.getEventInfoByRctLink(epgTrailerLinkInfo);
	}

	/**
	 * Eable EPG Barker channel work.
	 *
	 * @return boolean
	 * @throws TvCommonException
	 */
	public boolean enableEpgBarkerChannel() throws TvCommonException
	{
		return epgMgr.enableEpgBarkerChannel();
	}

	/**
	 * Disable EPG Barker channel work.
	 *
	 * @return boolean
	 * @throws TvCommonException
	 */
	public boolean disableEpgBarkerChannel() throws TvCommonException
	{
		return epgMgr.disableEpgBarkerChannel();
	}

	/**
	 * Get offset time for event time between time of offset time change case
	 *
	 * @param utcTime
	 *            Time UTC time
	 * @param isStartTime
	 *            boolean TRUE: event start time, FALSE: event end time
	 * @return int
	 * @throws TvCommonException
	 */
	public int getEpgEventOffsetTime(Time utcTime, boolean isStartTime) throws TvCommonException
	{
		return epgMgr.getEpgEventOffsetTime(utcTime, isStartTime);
	}

	/**
	 * Get present/following event information of certain service
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNo
	 *            int Program service number.
	 * @param present
	 *            boolean
	 * @param eventComponentInfo
	 *            DtvEventComponentInfo
	 * @param descriptionType
	 *            EnumEpgDescriptionType
	 * @return EpgEventInfo
	 * @throws TvCommonException
	 */
	public PresentFollowingEventInfo getPresentFollowingEventInfo(short serviceType, int serviceNo, boolean present,
	        EnumEpgDescriptionType descriptionType)
	{
		try
        {
	        return epgMgr.getPresentFollowingEventInfo(serviceType, serviceNo, present, descriptionType);
        }
        catch (TvCommonException e)
        {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        Log.d("TvApp","!!!getPresentFollowingEventInfo");
	        return null;
        }
	}

	/**
	 * Get first matched Epg HD simulcast.
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNo
	 *            int Program service number.
	 * @param baseTime
	 *            Time Event start time based on UTC time.
	 * @return Epg1stMatchHDSimulcastVO including if resolvable,target program
	 *         name, target event
	 * @throws TvCommonException
	 */
	public EpgFirstMatchHdCast getEvent1stMatchHdSimulcast(short serviceType, int serviceNo, Time baseTime)
	        throws TvCommonException
	{
		return epgMgr.getEvent1stMatchHdSimulcast(serviceType, serviceNo, baseTime);
	}

	/**
	 * Get first matched EPG HD broadcast later.
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNo
	 *            int Program service number.
	 * @param baseTime
	 *            Time Event start time base on UTC time.
	 * @return Epg1stMatchHDSimulcastVO including if resolvable,target program
	 *         name, target event
	 * @throws TvCommonException
	 */
	public EpgFirstMatchHdCast getEvent1stMatchHdBroadcast(short serviceType, int serviceNo, Time baseTime)
	        throws TvCommonException
	{
		return epgMgr.getEvent1stMatchHdBroadcast(serviceType, serviceNo, baseTime);
	}

	/**
	 * Get EIT information.
	 *
	 * @return DtvEitInfo The EIT information.
	 * @throws TvCommonException
	 */
	public DtvEitInfo getEitInfo(boolean bPresent) throws TvCommonException
	{
		return epgMgr.getEitInfo(bPresent);
	}

	/**
	 * Setting the service priority for the case EPG database is full. the EIT
	 * buffer assigned to the service with lowest priority will be replaced.
	 * CM/UI should call this function whenever channel change.
	 *
	 * @param programIndex
	 *            int service position or program index.
	 * @throws TvCommonException
	 */
	public void setEpgProgramPriority(int programIndex) throws TvCommonException
	{
		epgMgr.setEpgProgramPriority(programIndex);
	}

	/**
	 * Setting the service priority for the case EPG database is full. the EIT
	 * buffer assigned to the service with lowest priority will be replaced.
	 * CM/UI should call this function whenever channel change.
	 *
	 * @param serviceType
	 *            short Program service type. (DTV, radio, data service)
	 * @param serviceNo
	 *            int Program service number.
	 * @throws TvCommonException
	 */
	public void setEpgProgramPriority(short serviceType, int serviceNo) throws TvCommonException
	{
		epgMgr.setEpgProgramPriority(serviceType, serviceNo);
	}

	/**
	 * Get NVOD time shift event counts belong to the input NVOD reference
	 * service
	 *
	 * @param serviceType
	 *            short nvod program service type.
	 * @param serviceNumber
	 *            int nvod program service number.
	 * @return int the found events info count
	 * @throws TvCommonException
	 */
	public int getNvodTimeShiftEventCount(short serviceType, int serviceNumber) throws TvCommonException
	{
		return epgMgr.getNvodTimeShiftEventCount(serviceType, serviceNumber);
	}

	/**
	 * Get NVOD time shift event info service
	 *
	 * @param serviceType
	 *            short nvod program service type.
	 * @param serviceNumber
	 *            int nvod program service number.
	 * @return int the found events info count
	 * @throws TvCommonException
	 */
	public ArrayList<NvodEventInfo> getNvodTimeShiftEventInfo(short serviceType, int serviceNumber, int maxEventNum,
	        EnumEpgDescriptionType eEpgDescritionType) throws TvCommonException
	{
		return epgMgr.getNvodTimeShiftEventInfo(serviceType, serviceNumber, maxEventNum, eEpgDescritionType);
	}

	/**
	 * Add epg event
	 *
	 * @param vo
	 *            EpgEventTimerInfo
	 *
	 * @return EnumEpgTimerCheck
	 *
	 * @throws TvCommonException
	 */
	public TvOsType.EnumEpgTimerCheck addEpgEvent(EpgEventTimerInfo vo) throws TvCommonException
	{
		return tm.addEpgEvent(vo);
	}

	/**
	 * get epg timer event by index
	 *
	 * @param index
	 *            int
	 *
	 * @return EpgEventTimerInfo timer event info
	 *
	 * @throws TvCommonException
	 */
	public EpgEventTimerInfo getEpgTimerEventByIndex(int index) throws TvCommonException
	{
		return tm.getEpgTimerEventByIndex(index);
	}

	/**
	 * get epg timer event count
	 *
	 * @return int timer event count
	 *
	 * @throws TvCommonException
	 */
	public int getEpgTimerEventCount() throws TvCommonException
	{
		return tm.getEpgTimerEventCount();
	}

	/**
	 * is Epg TimerSetting Valid
	 *
	 * @param timerInfoVo
	 *            EpgEventTimerInfo.
	 *
	 * @return TvOsType.EnumEpgTimerCheck
	 *
	 * @throws TvCommonException
	 */
	public TvOsType.EnumEpgTimerCheck isEpgTimerSettingValid(EpgEventTimerInfo timerInfoVo) throws TvCommonException
	{
		return tm.isEpgTimerSettingValid(timerInfoVo);
	}

	/**
	 * del All Epg Event
	 *
	 * @return boolean
	 *
	 * @throws TvCommonException
	 */
	public boolean delAllEpgEvent() throws TvCommonException
	{
		return tm.delAllEpgEvent();
	}

	/**
	 * is Epg TimerSetting Valid
	 *
	 * @param epgEvent
	 *            int.
	 *
	 * @return boolean
	 *
	 * @throws TvCommonException
	 */
	public boolean delEpgEvent(int epgEvent) throws TvCommonException
	{
		return tm.delEpgEvent(epgEvent);
	}

	/**
	 * delete Past Epg Timer
	 *
	 * @return boolean
	 *
	 * @throws TvCommonException
	 */
	public boolean deletePastEpgTimer() throws TvCommonException
	{
		return tm.deletePastEpgTimer();
	}

	/**
	 * exec Epg Timer Action
	 *
	 * @return boolean
	 *
	 * @throws TvCommonException
	 */
	public boolean execEpgTimerAction() throws TvCommonException
	{
		return tm.execEpgTimerAction();
	}

    /**
     * Reconfig EPG timer list & setting monitors
     *
     * @param timeActing int (the time before valid list items.)
     * @param checkEndTime boolean (delete according to end time.)
     * @throws TvCommonException
     */
    public void cancelEpgTimerEvent(int timeActing,
        boolean checkEndTime) throws TvCommonException
    {
        tm.cancelEpgTimerEvent(timeActing, checkEndTime);
    }
	/**
	 * Get timer recording program.
	 *
	 * @return EpgEventTimerInfo The current timer info
	 * @throws TvCommonException
	 */
	public EpgEventTimerInfo getEpgTimerRecordingProgram() throws TvCommonException
	{
		return tm.getEpgTimerRecordingProgram();
	}

	/**
	 * Gets timezone state
	 *
	 * @return TvOsType.EnumTimeZone (Timezone state)
	 * @throws TvCommonException
	 */
	public TvOsType.EnumTimeZone getTimeZone() throws TvCommonException
	{
		return tm.getTimeZone();
	}
}