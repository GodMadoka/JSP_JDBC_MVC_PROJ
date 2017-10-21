package com.web.jdbc;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "StudentControllerServlet", urlPatterns = "/web/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
    private StudentDbUtil studentDbUtil;

    @Resource(name="jdbc/web_student_tracker")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        super.init();

        // create our student db util ... and pass in the conn pool / data source
        try {
            studentDbUtil = new StudentDbUtil(dataSource);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // read the "command" parameter
            String theCommand = request.getParameter("command");
            // if the command is missing, then default to listing students
            if (theCommand == null) {
                theCommand = "LIST";
            }
            // route to the appropriate method
            switch (theCommand) {
                case "LIST":
                    // list the students ... in MVC fashion
                    listStduents(request,response);
                    break;
                case "LOAD":
                    loadStudent(request,response);
                    break;
                case "UPDATE":
                    updateStudent(request,response);
                    break;
                case "DELETE":
                    deleteStudent(request,response);
                    break;
                case "SEARCH":
                    searchStudents(request,response);
                    break;
                default:
                    listStduents(request,response);
            }
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // read the "command" parameter
            String theCommand = request.getParameter("command");
            // route the appropriate method
            switch (theCommand) {
                case "ADD":
                    addStudent(request,response);
                    break;
                default:
                    listStduents(request,response);
            }
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
        // read student id from form data
        int theStudentId = Integer.parseInt(request.getParameter("studentId"));
        // delete student from database
        studentDbUtil.deleteStudent(theStudentId);
        // send the back to "list students" page
        listStduents(request,response);
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
        // read student info from form data
        int id = Integer.parseInt(request.getParameter("studentId"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        // create a new student object
        Student theStudent = new Student(id,firstName,lastName,email);
        // perform update on database
        studentDbUtil.updateStudent(theStudent);
        // send them back to the "list students" page
        listStduents(request,response);
    }

    private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // read student id from form data
        String theStudentId = request.getParameter("studentId");
        // get student from database (db util)
        Student theStudent = studentDbUtil.getStudent(theStudentId);
        // place student in the request attribute
        request.setAttribute("THE_STUDENT",theStudent);
        // send to jsp page: update-student-form.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
        dispatcher.forward(request,response);
    }

    private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // read student info from form data
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        // create a new student object
        Student theStudent = new Student(firstName,lastName,email);
        // add the student to the database
        studentDbUtil.addStudent(theStudent);
        // send back to main page (the student list)
        // SEND AS REDIRECT to avoid multiple-browser reload issue
        response.sendRedirect(request.getContextPath()+"/StudentControllerServlet?command=LIST");
    }

    private void listStduents(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // get student from db util
        List<Student> students = studentDbUtil.getStudents();
        // add students to the request
        request.setAttribute("STUDENT_LIST",students);
        // send to JSP page (view)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request,response);
    }

    private void searchStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // read search name from form data
        String theSearchName = request.getParameter("theSearchName");
        // search students from db util
        List<Student> students = studentDbUtil.searchStudents(theSearchName);
        // add students to the request
        request.setAttribute("STUDENT_LIST",students);
        // send to JSP page (view)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request,response);
    }
}