
using System;
namespace RBS.CodeComp.Client.IO
{
    public interface IWriter
    {
        void Info(string info, bool newLine = false, ConsoleColor color = ConsoleColor.Black);
    }
}
