// 관리자 로그인 버튼
const loginButton = document.getElementById("loginButton");

// 관리자 비밀번호 입력창
const adminPassword = document.getElementById("adminPassword");

// 로그인 결과 메시지
const loginMessage = document.getElementById("loginMessage");

// 관리자 기능 영역
const adminArea = document.getElementById("adminArea");

// 로그인 영역
const loginArea = document.getElementById("loginArea");


// 로그인 버튼을 눌렀을 때
loginButton.addEventListener("click", async () => {

    // 입력한 관리자 비밀번호 가져오기
    const password = adminPassword.value;

    // 아무것도 입력하지 않았을 경우
    if (password === "") {
        loginMessage.textContent = "비밀번호를 입력해주세요.";
        return;
    }


    try {

        // Spring Boot에 관리자 로그인 요청
        const response = await fetch("/api/admin/login", {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                password: password
            })
        });


        // 로그인 성공
        if (response.ok) {

            loginMessage.textContent = "로그인 성공!";

            // 로그인 화면 숨기기
            loginArea.style.display = "none";

            // 관리자 기능 보여주기
            adminArea.style.display = "block";

            // 나중에 방명록 목록을 불러올 곳
            loadGuestbooks();

        }

        // 로그인 실패
        else {

            alert("관리자 비밀번호가 올바르지 않습니다.");

            // 이전 페이지로 돌아가기
            history.back();
        }

    } catch (error) {

        console.error("관리자 로그인 오류:", error);

        alert("서버와 연결할 수 없습니다.");

    }

});


// 관리자용 방명록 목록 불러오기
async function loadGuestbooks() {

    try {

        const response = await fetch("/guestbook/get");

        if (!response.ok) {
            throw new Error("방명록을 불러오지 못했습니다.");
        }

        const guestbooks = await response.json();

        const list = document.getElementById("guestbookList");

        // 기존 내용 삭제
        list.innerHTML = "";


        // 방명록 하나씩 표시
        guestbooks.forEach(guestbook => {

            const div = document.createElement("div");

            div.innerHTML = `
                <hr>

                <p>
                    <strong>이름:</strong>
                    ${guestbook.name}
                </p>

                <p>
                    <strong>평점:</strong>
                    ${guestbook.rating}
                </p>

                <p>
                    <strong>내용:</strong>
                    ${guestbook.content}
                </p>

                <button onclick="deleteGuestbook(${guestbook.id})">
                    관리자 삭제
                </button>
            `;

            list.appendChild(div);
        });

    } catch (error) {

        console.error(error);

        document.getElementById("guestbookList").textContent =
            "방명록을 불러오지 못했습니다.";
    }
}


// 관리자용 방명록 삭제
async function deleteGuestbook(id) {

    const result = confirm(
        "정말 이 방명록을 삭제하시겠습니까?"
    );

    if (!result) {
        return;
    }


    try {

        const response = await fetch( `/guestbook/admin-delete?id=${id}`, {
            method: "DELETE"
        });


        if (response.ok) {

            alert("삭제되었습니다.");

            // 삭제 후 목록 다시 불러오기
            loadGuestbooks();

        } else {

            alert("삭제에 실패했습니다.");

        }

    } catch (error) {

        console.error("삭제 오류:", error);

        alert("서버와 연결할 수 없습니다.");

    }
}