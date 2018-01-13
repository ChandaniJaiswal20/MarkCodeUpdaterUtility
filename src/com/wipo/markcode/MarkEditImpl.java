package com.wipo.markcode;

import org.apache.log4j.Logger;

public class MarkEditImpl {
	
	
	 private 	static Logger logger	=	Logger.getLogger(MarkEditImpl.class);
//	 public static CommonsProxyFactory commonsProxyFactory 	=	null;
	 public static String signWcode	= null;
//		static	IMark iMark	=	null;
		private String file_seq	;
		private String file_typ	;
		private int file_ser	;
		private int file_nbr	;
	//	private String signWcode	;
//		private CMark mark	;
		
		
		public String getFile_seq() {
			return file_seq;
		}


		public void setFile_seq(String file_seq) {
			this.file_seq = file_seq;
		}


		public String getFile_typ() {
			return file_typ;
		}


		public void setFile_typ(String file_typ) {
			this.file_typ = file_typ;
		}


		public int getFile_ser() {
			return file_ser;
		}


		public void setFile_ser(int file_ser) {
			this.file_ser = file_ser;
		}


		public int getFile_nbr() {
			return file_nbr;
		}


		public void setFile_nbr(int file_nbr) {
			this.file_nbr = file_nbr;
		}


		public static String getSign_wcode() {
			return signWcode;
		}


	

//		public CMark getMark() {
//			return mark;
//		}
//
//
//		public void setMark(CMark mark) {
//			this.mark = mark;
//		}
//		
//	
//
//		public CommonsProxyFactory getCommonsProxyFactory() {
//			return commonsProxyFactory;
//		}

		/**
	
	// get CMark instance on the basis of criteria set

		public  CMark getCurrentMark(){
				
				 CCriteriaMark cCriteriaMark =	null;
				 CCriteriaFileId fileId =	null;
				 CCriteriaStatus cCriteriaStatus	=	null;
				 CCriteriaProcessByAction criteriaProcessByAction = null;
				 CMark mark = null; 
				 iMark = commonsProxyFactory.getIMark();
				 cCriteriaMark = new CCriteriaMark();
				fileId = new CCriteriaFileId();
				fileId.setFileSeq(getFile_seq());
				fileId.setFileType(getFile_typ());
				fileId.setFileSeries(new IpasInteger(getFile_ser()));
				fileId.setFileNbrFrom(new IpasInteger(getFile_nbr()));
				fileId.setFileNbrTo(new IpasInteger(getFile_nbr()));
			
				cCriteriaMark.setCriteriaFileId(fileId);
			
				
						try {
							if(iMark==null){
								
								logger.debug("Please correct the GLASSFISH_HOME path in the run.bat file of the utility");
								logger.debug("Program exit!!!");
								System.exit(0);
							}
							
							CFileSummaryList fileSummaryList = iMark.mGetList(cCriteriaMark);
		
							//logger.debug("Total mark Count:"+fileSummaryList.size());
							if(fileSummaryList != null && !fileSummaryList.isEmpty()){
								for (CFileSummary fileSummary : fileSummaryList) {
									
									mark	=	iMark.mRead(fileSummary.getFileId(), true, false);
									
								}
							}
													
						} catch (IpasException e) {
							e.printStackTrace();
							logger.error("iMark.mRead() error:" + e.getMessage()+"\r\n");
							logger.debug("Program exit!!!");
							System.exit(0);
						}
						
						
			return mark;
		
}

			//update mark signType 
		public  void updateSignType(){
			
			logger.debug("going to update signtype.....");
			
			if (signWcode == null) {
				mark.getSignData().setSignType("N"); //$NON-NLS-1$
			} else {
				if (signWcode.equals("D")) { //$NON-NLS-1$
					mark.getSignData().setSignType("N"); //$NON-NLS-1$
				}
				if (signWcode.equals("F")) { //$NON-NLS-1$
					mark.getSignData().setSignType("L"); //$NON-NLS-1$
				}
				if (signWcode.equals("M")) { //$NON-NLS-1$
					mark.getSignData().setSignType("B"); //$NON-NLS-1$
				}
				if (signWcode.equals("T")) { //$NON-NLS-1$
					mark.getSignData().setSignType("T"); //$NON-NLS-1$
				}
				if (signWcode.equals("S")) { //$NON-NLS-1$
					mark.getSignData().setSignType("S"); //$NON-NLS-1$
				}
				if (signWcode.equals("O")) { //$NON-NLS-1$
					mark.getSignData().setSignType("O"); //$NON-NLS-1$
				}
			}
			
			
			
		
				try {
						iMark.mUpdate(mark, null);
						
				 } catch (IpasException e) {
					// TODO Auto-generated catch block
					
					logger.error("Exception occured while updating mark signType::"+mark.getMarkFormatted()+ e.getMessage()+"\r\n");
				//	e.printStackTrace();
				}
		logger.debug("Mark signType updated Sucessfully.....");
	
}
			
			

**/
			
			
			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return file_seq+"/"+file_typ+"/"+file_ser+"/"+file_nbr;
			}


	
	

}
