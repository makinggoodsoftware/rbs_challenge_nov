using System;
using System.IO;

namespace RBS.CodeComp.Client.IO
{
    public class Writer : IWriter
    {
        private string _teamName;
        public Writer(string teamName)
        {
            _teamName = teamName;
        }

        public void Info(string info, bool newLine = false, ConsoleColor color = ConsoleColor.Black)
        {
            Console.ForegroundColor = color;

            if (newLine) Console.WriteLine(info);
            else Console.Write(info);

            Console.ForegroundColor = ConsoleColor.Black;

            string text = newLine ? "\r\n" + info : info;
            File.AppendAllText(_teamName + "_" + @"GameOutput.txt", text);
        }
    }
}
