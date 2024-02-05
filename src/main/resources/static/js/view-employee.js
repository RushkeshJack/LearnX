document.addEventListener('DOMContentLoaded', function () {
    // Fetch employee data from the specified URL endpoint
    fetch('/LearnX/api/employee')  // Replace with your actual endpoint URL
        .then(response => response.json())
        .then(data => {
            // Update the table body with the fetched employee data
            updateEmployeeTable(data);
            console.log("data");
        })
        .catch(error => {
            console.error('Error fetching employee data:', error);
        });
});

// Function to update the table body with employee data
function updateEmployeeTable(employees) {
    const tbody = document.querySelector('#employeeTableBody');

    // Clear existing table rows
    tbody.innerHTML = '';

    // Iterate over the fetched employee data and create table rows
    employees.forEach(employee => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${employee.id}</td>
            <td>${employee.email}</td>
            <td>${employee.name}</td>
            <td>${employee.role}</td>
            <td>${employee.job_level}</td>
            <td>${employee.unit}</td>
            <td>${employee.office}</td>
            <td><a href="/LearnX/admin/view-employee/remove-access/${employee.id}">
                <i class="fa-solid fa-trash fa-lg" style="color: #ff0000;"></i>
            </a></td>`;
        tbody.appendChild(row);
    });
}
