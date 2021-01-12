﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MessageForm01
{
    public delegate void UserInfoEventHandler(object sender, UserInfoEventArgs e);
    public class UserInfoEventArgs : EventArgs
    {
        public string ID //
        {
            get;
            private set;
        }
        public string IPStr
        {
            get;
            private set;
        }
        public int SPort
        {
            get;
            private set;
        }
        public int FPort //파일
        {
            get;
            private set;
        }
        public UserInfoEventArgs(string id, string ipstr, int sport, int fport)
        {
            ID = id;
            IPStr = ipstr;
            SPort = sport;
            FPort = fport;
        }
        public override string ToString()
        {
            return ID;
        }
    }
}
